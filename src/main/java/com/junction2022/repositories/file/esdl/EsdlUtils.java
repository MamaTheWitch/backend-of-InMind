package com.junction2022.repositories.file.esdl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

import com.junction2022.common.exceptions.InvalidDataException;
import com.junction2022.common.utils.CountryUtils;
import com.junction2022.models.MentalService;
import com.junction2022.models.MentalServiceSet;
import com.junction2022.models.ServiceType;

import esdl.AggregatedBuilding;
import esdl.Area;
import esdl.AreaScopeEnum;
import esdl.Asset;
import esdl.EnergySystem;
import esdl.EsdlPackage;
import esdl.GenericBuilding;
import esdl.Instance;

public class EsdlUtils {

	public static final String FILE_EXTENSION = "esdl";


	public static EnergySystem loadEsdlModel(final String filePath) throws IOException {
        // Initialize the model
        EsdlPackage.eINSTANCE.eClass();

        final XMIResource resource = new XMIResourceImpl(URI.createFileURI(filePath));

        // speed up loading of large files by defering ID references lookup.
        resource
        	.getDefaultLoadOptions()
        	.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

        // TODO: Check why this line doesn't work
        //        resource.setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());


        // load the resource
        resource.load(null);

        return (EnergySystem) resource.getContents().get(0);
	}

	public static EnergySystem loadEsdlModel(final InputStream in) throws IOException {
		// Initialize the model
		EsdlPackage.eINSTANCE.eClass();

        final XMIResource resource = new XMIResourceImpl();

		// speed up loading of large files by defering ID references lookup.
		resource
			.getDefaultLoadOptions()
			.put(XMIResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);

		// TODO: Check why this line doesn't work
		//        resource.setIntrinsicIDToEObjectMap(new HashMap<String, EObject>());


		// load the resource
		resource.load(in, null);

		return (EnergySystem) resource.getContents().get(0);
	}

	public static XMIResource saveEsdlModel(final EnergySystem energySystem, final String filePath) throws IOException {
        final XMIResource resource = new XMIResourceImpl(URI.createFileURI(filePath));
        resource.getContents().add(energySystem);

        // Produce an xsi:schemaService in the resource
        resource.save(Map.of(XMIResource.OPTION_SCHEMA_LOCATION, true));

        return resource;
    }



	public static void normalizeAreaIds(final EnergySystem energySystem) {
		normalizeObjectIds(energySystem);
	}

	/**
	 * Set area's UUID if absent.
	 * @param area
	 */
	private static void normalizeObjectIds(final Object esdlObject) {
		if (esdlObject == null) {
			return;
		}

		if (esdlObject instanceof final List<?> list) {
			for (final Object item : list) {
				normalizeObjectIds(item);
			}
		} else if (esdlObject instanceof final EnergySystem energySystem) {
			normalizeObjectIds(energySystem.getInstance()); // list of Instance
		} else if (esdlObject instanceof final Instance instance) {
			// normalizeObjectId(instance::getId, instance::setId); // ignore instance
			normalizeObjectIds(instance.getArea()); // list of Area
		} else if (esdlObject instanceof final Area area) {
			area.setId(
					isCountry(area) ?
							getCountryUuid(area.getName()).toString() :
							getOrCreateObjectUuid(area.getId()));
			normalizeObjectIds(area.getArea()); // list of Area
			normalizeObjectIds(area.getAsset()); // list of Asset
		} else if (esdlObject instanceof final Asset asset) {
			asset.setId(getOrCreateObjectUuid(asset.getId()));
			try {
				final Method getAssetMethod = asset.getClass().getMethod("getAsset");
				normalizeObjectIds(getAssetMethod.invoke(asset)); // list of Asset
			} catch (final NoSuchMethodException | SecurityException | IllegalAccessException |
					IllegalArgumentException | InvocationTargetException e) {
				throw new InvalidDataException("Cannot get child assets from : " + asset.getName());
			}
		} else {
			throw new InvalidDataException("Unsupported ESDL object class: " + esdlObject.getClass());
		}
	}

	private static String getOrCreateObjectUuid(final String id) {
		if (id != null) {
			try {
				return UUID.fromString(id).toString();
			} catch (final IllegalArgumentException e) {
			}
		}
		return UUID.randomUUID().toString();
	}


	public static MentalServiceSet extractServices(
			final EnergySystem energySystem,
			final boolean ignoreUnknownServiceTypes,
			final Optional<String> defaultCountryName) {
		final List<MentalService> rootServices = toServices(energySystem.getInstance(), ignoreUnknownServiceTypes);
		final List<MentalService> countryServices =
				rootServices
					.stream()
					.filter(MentalService::isCountry)
					.toList();

		if (countryServices.size() < rootServices.size()) {
			if (!countryServices.isEmpty()) {
				throw new InvalidDataException("Mixed area structure: with and without country.");
			}

			final Optional<MentalService> defaultCountryService =
					defaultCountryName
						.map(EsdlUtils::createCountryService)
						.map(countryService -> {
							countryService.setChildren(rootServices);
							return countryService;
						});

			if (defaultCountryService.isEmpty()) {
				throw new InvalidDataException("Unknown default country: " + defaultCountryName.orElse(null));
			}

			return new MentalServiceSet(List.of(defaultCountryService.get()));
		}

		return new MentalServiceSet(countryServices);
	}


	private static List<MentalService> toServices(final Object esdlObject, final boolean ignoreUnknownServiceTypes) {
		if (esdlObject == null) {
			return List.of();
		} else if (esdlObject instanceof final List<?> esdlObjectList) {
			final List<MentalService> services = new ArrayList<>();
			for (final Object item : esdlObjectList) {
				services.addAll(toServices(item, ignoreUnknownServiceTypes));
			}
			return services;
		} else if (esdlObject instanceof final Instance instance) {
			return toServices(instance.getArea(), ignoreUnknownServiceTypes);
		} else if (esdlObject instanceof final Area area) {
			if (area.getScope() == AreaScopeEnum.CONTINENT) {
				return toServices(area.getArea(), ignoreUnknownServiceTypes);
			}

			return
					toService(
						ignoreUnknownServiceTypes,
						getServiceType(area, ignoreUnknownServiceTypes),
						area.getName(),
						area.getId(),
						area.getArea(),
						area.getAsset())
					.map(List::of) 			// one-item list
					.orElseGet(List::of); 	// empty list
		} else if (esdlObject instanceof final Asset asset) {
			return toService(
						ignoreUnknownServiceTypes,
						getServiceType(asset, ignoreUnknownServiceTypes),
						asset.getName(),
						asset.getId(),
						getChildAssets(asset))
					.map(List::of) 			// one-item list
					.orElseGet(List::of);	// empty list
		} else {
			throw new InvalidDataException("Unsupported ESDL object class: " + esdlObject.getClass());
		}
	}

	@SuppressWarnings("unchecked")
	private static List<Asset> getChildAssets(final Asset asset) {
		try {
			final Method getAssetMethod = asset.getClass().getMethod("getAsset");
			return (List<Asset>) getAssetMethod.invoke(asset); // list of Asset
		} catch (final NoSuchMethodException | SecurityException | IllegalArgumentException |
				IllegalAccessException | InvocationTargetException  e) {
			return List.of();
		}
	}

	private static Optional<MentalService> toService(
			final boolean ignoreUnknownServiceTypes,
			final Optional<ServiceType> serviceType,
			final String name,
			final String id,
			final Object... children) throws IllegalArgumentException
	{
		if (serviceType.isEmpty()) {
			return Optional.empty();
		}

		final MentalService service = new MentalService();
		service.setName(name);
		service.setType(serviceType.get());

		UUID uuid;
		try {
			uuid = UUID.fromString(id);
		} catch (final IllegalArgumentException | NullPointerException e) {
			if (!service.isCountry()) {
				throw new InvalidDataException(String.format("Area '%s' has an invalid UUID: %s", name, id));
			}

			uuid = getCountryUuid(name);
		}

		service.setUuid(uuid);

		if (children != null) {
			final List<MentalService> childrenServices = new ArrayList<>();
			for (final Object child : children) {
				childrenServices.addAll(toServices(child, ignoreUnknownServiceTypes));
			}

			if (!childrenServices.isEmpty()) {
				// set unmodifiable list
				service.setChildren(List.copyOf(childrenServices));
			}
		}

		return Optional.of(service);
	}

	private static boolean isCountry(final Area area) {
		return area.getScope() == AreaScopeEnum.COUNTRY;
	}

	public static Optional<ServiceType> getServiceType(final Area area, final boolean ignoreUnknownServiceTypes) {
		switch (area.getScope()) {
		case COUNTRY:
			return Optional.of(ServiceType.COUNTRY);
		case CITY:
			return Optional.of(ServiceType.CITY);
		case DISTRICT:
			return Optional.of(ServiceType.DISTRICT);
		case NEIGHBOURHOOD:
			return Optional.of(ServiceType.BUILDING_BLOCK);
		case BUILDING:
			return Optional.of(ServiceType.BUILDING);
		default:
			if (ignoreUnknownServiceTypes) {
				return Optional.empty();
			}
			throw new InvalidDataException("Unexpected area scope value: " + area.getScope());
		}
	}

	public static Optional<ServiceType> getServiceType(final Asset asset, final boolean ignoreUnknownServiceTypes) {
		if (asset instanceof AggregatedBuilding) {
			return Optional.of(ServiceType.BUILDING_BLOCK);
		} else if (asset instanceof GenericBuilding) { // Building or BuildingUnit
			return Optional.of(ServiceType.BUILDING);
		} else if (ignoreUnknownServiceTypes) {
			return Optional.empty();
		}
		throw new InvalidDataException("Unexpected asset class: " + asset.getClass());
	}

	private static MentalService createCountryService(final String countryNameOrCode) {
		return CountryUtils
					.findByNameOrCode(countryNameOrCode)
					.map(countryCode -> {
						final MentalService countryService = new MentalService();
						countryService.setName(countryCode.getName());
						countryService.setType(ServiceType.COUNTRY);
						countryService.setUuid(CountryUtils.convertToUuid(countryCode));
						return countryService;
					})
					.orElseThrow(() -> new InvalidDataException("Unknown country: " + countryNameOrCode));
	}

	private static UUID getCountryUuid(final String countryNameOrCode) {
		return CountryUtils
					.findByNameOrCode(countryNameOrCode)
					.map(CountryUtils::convertToUuid)
					.orElseThrow(() -> new InvalidDataException("Unknown country: " + countryNameOrCode));
	}

	private EsdlUtils() {
	}

}
