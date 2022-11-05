package com.junction2022.views.rest;
//package fi.vtt.bimlinker.web.test;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.function.BiFunction;
//
//import org.apache.jena.query.QueryExecutionFactory;
//import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.ResultSet;
//import org.apache.jena.rdf.model.Model;
//import org.apache.jena.rdf.model.ModelFactory;
//import org.apache.jena.rdf.model.Property;
//import org.apache.jena.rdf.model.Resource;
//import org.apache.jena.rdf.model.ResourceFactory;
//import org.apache.jena.vocabulary.RDF;
//import org.apache.logging.log4j.Level;
//import org.springframework.stereotype.Service;
//
//import com.github.jsonldjava.shaded.com.google.common.collect.ImmutableMap;
//
//import fi.vtt.bimlinker.data.dico.DicoOntology;
//import fi.vtt.bimlinker.web.rest.repositories.BimLinkerRepository;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.log4j.Log4j2;
//
///**
// *
// * @author Nam Vu
// *
// */
//@MentalService
//@Log4j2
//public class TestMutationResolver extends BimLinkerRepository {
//
//	public static class TestDicoOntology {
//		public static class Collections {
//
//			public static final String NAMESPACE_PREFIX = "diccol";
//			public static final String NAMESPACE_URI = DicoOntology.BASE_URI + "Collections#";
//
//			public static final Resource List = ResourceFactory.createResource(NAMESPACE_URI + "List");
//
//			public static final Property hasIndex = ResourceFactory.createProperty(NAMESPACE_URI, "hasIndex");
//			public static final Property hasSlot = ResourceFactory.createProperty(NAMESPACE_URI, "hasSlot");
//			public static final Property hasValue = ResourceFactory.createProperty(NAMESPACE_URI, "hasValue");
//
//			private Collections() {}
//		}
//	}
//
//
//
//	private static final int TEST_LIST_SIZE = 100000;
//
//	@Getter @Setter
//	public static class TestOrderedListOutput {
//		private List<Long> original;
//		private List<Long> processed;
//	}
//
//	public TestOrderedListOutput setOrderedList(List<Integer> input) {
//
//		log.info("Creating dummy list");
//		input = createDummyList();
//
//		final int testCount = 100;
//
//		long sum1 = 0L;
//		long sum2 = 0L;
//
//		final Model model = ModelFactory.createDefaultModel();
//
//		for (int i = 1; i <= testCount; ++i) {
//			log.info("{}. Writing and reading (method 1)", i);
//			sum1 += runTest(model, input, this::writeAndRead1);
//
//			log.info("{}. Writing and reading (method 2)", i);
//			sum2 += runTest(model, input, this::writeAndRead2);
//
//			if (i % 10 == 0) {
//				log.printf(Level.INFO,
//						"""
//						Average time:
//						duration1=%,d
//						duration2=%,d
//						""",
//						sum1 / i,
//						sum2 / i);
//			}
//		}
//
//
//		return null;
//	}
//
//	private static long runTest(final Model model, final List<Integer> list, final BiFunction<Model, List<Integer>, Long> consumer) {
//		model.removeAll();
//
//		final long duration = consumer.apply(model, list);
//		log.printf(Level.INFO, "\tProcessing duration=%,d (nanoseconds), Model size=%,d (triples)", duration, model.size());
//
//		return duration;
//	}
//
//
//	private static List<Integer> createDummyList() {
//		final List<Integer> list = new ArrayList<>(TEST_LIST_SIZE);
//		for (int i = 1; i <= TEST_LIST_SIZE; ++i) {
//			list.add(i);
//		}
//		return list;
//	}
//
//	private long writeAndRead1(final Model model, final List<Integer> input) {
//		long start = System.nanoTime();
//
//		final Resource sequenceResource =
//				model
//					.createResource()
//					.addProperty(RDF.type, RDF.Seq);
//
//		int index = 0;
//		for (final Integer value : input) {
//			sequenceResource.addLiteral(RDF.li((++index) * 10), value);
//		};
//
//		long duration = System.nanoTime() - start;
//
//
//
//		final var sparql =
//				createParameterizedSparqlString(
//						"""
//						SELECT ?value ?index {
//								[] a rdf:Seq ;
//									?p ?value .
//								FILTER(CONTAINS(STR(?p), "_")) .
//								BIND (STRDT(STRAFTER(STR(?p), "_"), xsd:integer) AS ?index) .
//						}
//						ORDER BY ?index
//						""",
//						null);
//
//		try (final var queryExecution = QueryExecutionFactory.create(sparql.asQuery(), model)) {
//
//			final ResultSet resultSet = queryExecution.execSelect();
//			start = System.nanoTime();
//			while (resultSet.hasNext()) {
//				final QuerySolution querySolution = resultSet.next();
//				final int value = querySolution.getLiteral("value").getInt();
//				index = querySolution.getLiteral("index").getInt();
//				if (index != value * 10) {
//					throw new IllegalArgumentException(String.format("Value: %d, index: %d", value, index));
//				}
//			}
//
//			duration += System.nanoTime() - start;
//		}
//
//		return duration;
//	}
//
//	private long writeAndRead2(final Model model, final List<Integer> input) {
//
//		long start = System.nanoTime();
//
//		final Resource listResource =
//				model
//					.createResource()
//					.addProperty(RDF.type, TestDicoOntology.Collections.List);
//
//		int index = 0;
//		for (final Integer value : input) {
//			final Resource slotResource =
//					model
//						.createResource()
//						.addLiteral(TestDicoOntology.Collections.hasIndex, (++index) * 10)
//						.addLiteral(TestDicoOntology.Collections.hasValue, value);
//
//			listResource.addProperty(TestDicoOntology.Collections.hasSlot, slotResource);
//		};
//
//		long duration = System.nanoTime() - start;
//
//
//		final var sparql =
//				createParameterizedSparqlString(
//						"""
//						SELECT ?value ?index {
//								[] a diccol:List ;
//									diccol:hasSlot [
//										diccol:hasValue ?value ;
//										diccol:hasIndex ?index
//									] .
//						}
//						ORDER BY ?index
//						""",
//						Map.of(TestDicoOntology.Collections.NAMESPACE_PREFIX, TestDicoOntology.Collections.NAMESPACE_URI));
//
//		try (final var queryExecution = QueryExecutionFactory.create(sparql.asQuery(), model)) {
//			final ResultSet resultSet = queryExecution.execSelect();
//
//			start = System.nanoTime();
//			while (resultSet.hasNext()) {
//				final QuerySolution querySolution = resultSet.next();
//				final int value = querySolution.getLiteral("value").getInt();
//				index = querySolution.getLiteral("index").getInt();
//				if (index != value * 10) {
//					throw new IllegalArgumentException(String.format("Value: %d, index: %d", value, index));
//				}
//			}
//
//			duration = System.nanoTime() - start;
//		}
//
//		return duration;
//	}
//
//
//}
