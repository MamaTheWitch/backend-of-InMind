package com.junction2022.views.graphql.resolvers.mutation;

import java.io.IOException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.junction2022.models.MentalServiceSet;
import com.junction2022.repositories.file.esdl.EsdlUtils;

import esdl.EnergySystem;

@Service
public class EsdlMutationResolver {

	public EnergySystem loadEsdlModel(final String filePath) throws IOException {
		return EsdlUtils.loadEsdlModel(filePath);
	}

	public EnergySystem normalizeEsdlModel(final String filePath, final Optional<String> newFilePath) throws IOException {
		final EnergySystem energySystem = EsdlUtils.loadEsdlModel(filePath);
		EsdlUtils.normalizeAreaIds(energySystem);

		EsdlUtils.saveEsdlModel(energySystem, newFilePath.orElse(filePath));
		return energySystem;
	}

	public MentalServiceSet loadEsdlModelAsServiceSet(
			final String filePath,
			final boolean ignoreUnknownServiceTypes,
			final Optional<String> defaultCountryName) throws IOException {
		final EnergySystem energySystem = loadEsdlModel(filePath);
		return EsdlUtils.extractServices(energySystem, ignoreUnknownServiceTypes, defaultCountryName);
	}


}
