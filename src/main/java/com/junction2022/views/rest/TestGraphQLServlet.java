package com.junction2022.views.rest;
//package fi.vtt.bimlinker.web.test;
//
//import graphql.kickstart.servlet.GraphQLConfiguration;
//import graphql.kickstart.servlet.GraphQLHttpServlet;
//import graphql.schema.GraphQLSchema;
//import graphql.schema.StaticDataFetcher;
//import graphql.schema.idl.RuntimeWiring;
//import graphql.schema.idl.SchemaGenerator;
//import graphql.schema.idl.SchemaParser;
//import graphql.schema.idl.TypeDefinitionRegistry;
//import lombok.extern.log4j.Log4j2;
//
///**
// *
// * @author Nam Vu
// *
// */
////@WebServlet(name = "TestGraphQLServlet", urlPatterns = { "/test-graphql" }, loadOnStartup = 1)
//@Log4j2
//@SuppressWarnings("serial")
//public class TestGraphQLServlet extends GraphQLHttpServlet {
//
//	@Override
//	protected GraphQLConfiguration getConfiguration() {
//		log.info("Registering 'test-graphql/*'");
//		return GraphQLConfiguration.with(createSchema()).build();
//	}
//
//	private static GraphQLSchema createSchema() {
//		final String schema = "type Query{hello: String}";
//
//		final SchemaParser schemaParser = new SchemaParser();
//		final TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(schema);
//
//		final RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
//				.type("Query", builder -> builder.dataFetcher("hello", new StaticDataFetcher("world"))).build();
//
//		final SchemaGenerator schemaGenerator = new SchemaGenerator();
//		return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
//	}
//
//}