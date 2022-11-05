package com.junction2022.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.junction2022.common.exceptions.InvalidDataException;

import lombok.Getter;

public class MentalServiceSet {

	@Getter
	private List<MentalService> rootServices;

	private Map<UUID, MentalService> flatServiceMap;

	public MentalServiceSet(final List<MentalService> rootServices) {
		this.rootServices = List.copyOf(rootServices);
		setServiceParents(rootServices, Optional.empty());
	}

	/**
	 * Imports all services from other service sets.
	 * @param serviceSets MentalService sets to be imported.
	 * @throws InvalidDataException If services with same UUID but different names found.
	 */
	public void addAll(final MentalServiceSet... serviceSets)
			throws InvalidDataException
	{
		for (final MentalServiceSet serviceSet : serviceSets) {
			rootServices = mergeLists(rootServices, serviceSet.getRootServices());
		}
	}

	/**
	 * Merge two service lists by comparing services one by one then return an unmodifiable service list.
	 * @param services1
	 * @param services2
	 * @return An unmodifiable list of merged services.
	 * @throws InvalidDataException If services with same UUID but different names found.
	 */
	private static List<MentalService> mergeLists(final List<MentalService> services1, final List<MentalService> services2)
			throws InvalidDataException
	{
		final List<MentalService> mergedServices = new ArrayList<>(services1);

		for (final MentalService service2 : services2) {
			final MentalService sameService1 =
					services1
						.stream()
						.filter(l -> l.getUuid().equals(service2.getUuid()))
						.findFirst()
						.orElse(null);

			if (sameService1 != null) {
				if (sameService1.getName().equals(service2.getName())) {
					final List<MentalService> mergedChildren =
							mergeLists(sameService1.getChildren(), service2.getChildren());
					setServiceParents(mergedServices, Optional.of(sameService1));
					sameService1.setChildren(mergedChildren);
				} else {
					throw new InvalidDataException(
							String.format(
									"Services with same UUID '%s' have difference names: '%s' vs '%s'",
									sameService1.getUuid(),
									sameService1.getName(),
									service2.getName()));
				}
			} else {
				mergedServices.add(service2);
			}
		}

		return mergedServices.size() != services1.size() ? List.copyOf(mergedServices) : services1;
	}

	private static void setServiceParents(final List<MentalService> services, final Optional<MentalService> parentService) {
		if (services != null) {
			for (final MentalService service : services) {
				service.setParentService(parentService);
				setServiceParents(service.getChildren(), Optional.of(service));
			}
		}
	}


	public Map<UUID, MentalService> getFlatServiceMap() {
		if (flatServiceMap == null) {
			flatServiceMap = asServiceMap(getRootServices());
		}
		return flatServiceMap;
	}


	private static Map<UUID, MentalService> asServiceMap(final List<MentalService> services) {
		final Map<UUID, MentalService> serviceMap = new HashMap<>();
		for (final MentalService service : services) {
			serviceMap.put(service.getUuid(), service);
			final List<MentalService> children = service.getChildren();
			if (children != null) {
				serviceMap.putAll(asServiceMap(children));
			}
		}
		return serviceMap;
	}


	public Optional<MentalService> getService(final UUID serviceUuid) {
		return Optional.ofNullable(getFlatServiceMap().get(serviceUuid));
	}



}
