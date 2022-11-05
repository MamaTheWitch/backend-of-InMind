package com.junction2022.models;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.jena.ontology.OntClass;
import org.geojson.GeoJsonObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.junction2022.repositories.rdf.FreshAirOntology;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentalService extends FreshAirEntity implements Comparable<MentalService> {

	@JsonIgnore
	private Optional<MentalService> parentService;

	private String code;
	private String name;
	private String description;

	private ServiceType type;

	private String geoJsonId;
	private GeoJsonObject geoJsonData;

	private List<MentalService> children;

	public boolean isCountry() {
		return ServiceType.COUNTRY.equals(type);
	}

	public Map<String, String> asShortMap() {
		if (parentService.isPresent()) {
			return
				Map.of(
					FreshAirVocabulary.PARENT_UUID_FIELD,
						getParentService()
							.map(MentalService::getUuid)
							.map(UUID::toString)
							.get(),
					FreshAirVocabulary.UUID_FIELD, getUuid().toString(),
					FreshAirVocabulary.NAME_FIELD, getName(),
					FreshAirVocabulary.TYPE_FIELD, getType().toString());
		} else {
			return
				Map.of(
					FreshAirVocabulary.UUID_FIELD, getUuid().toString(),
					FreshAirVocabulary.NAME_FIELD, getName(),
					FreshAirVocabulary.TYPE_FIELD, getType().toString());
		}
	}

	@Override
	public int compareTo(final MentalService o) {
		if (o == null) {
			return 1;
		}


		int res;
		if ((res = getType().compareTo(o.getType())) != 0) {
			return res;
		}

		if (getParentService().isPresent()) {
			if ((res = getParentService().get().compareTo(o.getParentService().orElse(null))) != 0) {
				return res;
			}
		} else if (o.getParentService().isPresent()) {
			return -1;
		}


		if ((res = getName().compareTo(o.getName())) != 0) {
			return res;
		}

		return 0;
	}

	@Override
	public OntClass getTypeRef() {
		return FreshAirOntology.Metadata.Service;
	}


}
