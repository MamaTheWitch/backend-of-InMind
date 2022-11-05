package com.junction2022.views.graphql.config;
//package com.junction2022.views.graphql.config;
//
//import java.util.Map;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Controller;
//
//import graphql.GraphQLContext;
//import graphql.kickstart.autoconfigure.tools.SchemaDirective;
//import graphql.schema.DataFetcher;
//import graphql.schema.DataFetcherFactories;
//import graphql.schema.DataFetchingEnvironment;
//import graphql.schema.GraphQLFieldDefinition;
//import graphql.schema.GraphQLFieldsContainer;
//import graphql.schema.idl.SchemaDirectiveWiring;
//import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
//import graphql.schema.idl.SchemaGeneratorDirectiveHelper;
//import graphql.schema.idl.SchemaGeneratorHelper;
//import lombok.extern.log4j.Log4j2;
//
//@Controller
//@Log4j2
//public class AuthenticationConfiguration {
//
//	public static class AuthorisationDirective implements SchemaDirectiveWiring {
//
////		@Override
////		  public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
////		    GraphQLFieldDefinition field = env.getElement();
////		    DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(field.getDataFetcher(), {
////		      dataFetchingEnvironment, value ->
////		        if (value == null) {
////		            return null
////		        }
////		        return  ((String) value).toUpperCase()
////		    });
////		    return field.transform({ builder -> builder.dataFetcher(dataFetcher) });
////		  }
//
//	    @Override
//	    public GraphQLFieldDefinition onField(final SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> environment) {
//	    	log.warn("Directives: {}", environment.getDirectives());
////	    	return environment.getElement();
//
//	    	Object targetRole =
//	        		environment
//	        			.getDirective()
//	        			.getArgument("role")
//	        			.getArgumentValue()
//	        			.getValue();
//
//	    	GraphQLFieldDefinition field = environment.getElement();
//	        GraphQLFieldsContainer parentType = environment.getFieldsContainer();
//	        //
//	        // build a data fetcher that first checks authorisation roles before then calling the original data fetcher
//	        //
//	        DataFetcher originalDataFetcher = environment.getCodeRegistry().getDataFetcher(parentType, field);
//	        DataFetcher authDataFetcher = new DataFetcher() {
//	            @Override
//	            public Object get(DataFetchingEnvironment dataFetchingEnvironment) throws Exception {
////	                GraphQLContext context = dataFetchingEnvironment.getGraphQlContext();
////	                Map<String, Object> contextMap = dataFetchingEnvironment.con.getContext();
//	            	Object authConext = dataFetchingEnvironment.getGraphQlContext().get("authContext");
//	                log.warn("ContextMap: {}", authConext);
////	                log.warn("authContext: {}", contextMap.get("authContext"));
////	                AuthorisationCtx authContext = (AuthorisationCtx) contextMap.get("authContext");
////
////	                if (authContext.hasRole(targetAuthRole)) {
//	                    return originalDataFetcher.get(dataFetchingEnvironment);
////	                } else {
////	                    return null;
////	                }
//	            }
//	        };
//	        //
//	        // now change the field definition to have the new authorising data fetcher
//	        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
//	        return field;
//	    }
//	}
//
//	@Bean
//	public SchemaDirective defineAuthDirective() {
//		return new SchemaDirective("auth", new AuthorisationDirective());
//	}
//
//}
