/**
 * Copyright 2000-2012 NeuStar, Inc. All rights reserved.
 * NeuStar, the Neustar logo and related names and logos are registered
 * trademarks, service marks or tradenames of NeuStar, Inc. All other
 * product names, company names, marks, logos and symbols may be trademarks
 * of their respective owners.
 */

package biz.neustar.service.metrics.ws.model;

import java.util.Calendar;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import biz.neustar.service.metrics.operation.CompositeOperation;
import biz.neustar.service.metrics.operation.RawRecordsOperation;
import biz.neustar.service.metrics.operation.SumOperation;

public class QueryCriteriaBuilder
{
	public static QueryCriteria buildQueryCriteria( QueryRequest query )
	{
		QueryCriteria qc = new QueryCriteria( );

		// TODO: Figure out how to get the real ts format
		/*
		String ts = ""
				+ DatatypeConverter.parseDateTime( query.getTs( ) )
						.getTimeInMillis( );
		String te = ""
				+ DatatypeConverter.parseDateTime( query.getTe( ) )
						.getTimeInMillis( );
		*/
		
		qc.setTs( DatatypeConverter.parseDateTime( query.getTs( ) ).getTimeInMillis( ) );
		qc.setTe(  DatatypeConverter.parseDateTime( query.getTe( ) ).getTimeInMillis( ) );

		List<String> contexts = query.getContexts( );
		for( String context : contexts )
		{
			// TODO: This is another instance where it would be nice to have a
			// programmatic way to
			// retrieve the required metrics context field names.
			ContextKVPair kvPair = new ContextKVPair( context );
			String key = kvPair.getContextKey( );
			if( key.equals(  Metric.FROM  ) )
				qc.setFrom( kvPair.getContextValue( ) );
			else if( key.equals( Metric.HOST ) )
				qc.setHost( kvPair.getContextValue( )  );
			else if( key.equals(  Metric.INSTANCE ) )
				qc.setInstance( kvPair.getContextValue( )  );
			else if( key.equals( Metric.PROCESS ) )
				qc.setProcess( kvPair.getContextValue( )  );
			else if( key.equals( Metric.RESOURCE ) )
				qc.setResource( kvPair.getContextValue( ) );
			else if( key.equals( Metric.SOURCE ) )
				qc.setSource( kvPair.getContextValue( ) );
			else
			{
				// This is a non-reserved context, so just stick it in the other/any field.
				qc.addOther( key, kvPair.getContextValue( )  );
			}
				
		}
		
		// TODO: Figure out how we're actually going to determine which operations to create.  This
		// definition conflicts with CompositeOperation.
		// Currently we're going to support the following:
		//		Sum
		
		//int opsCount = 0;
		//CompositeOperation ops = new CompositeOperation( );
		
		List<String> stats = query.getStats( );
		List<String> metrics = query.getMetrics( );
		for( String stat : stats )
		{
			if( stat.equalsIgnoreCase( "SUM" ) )
			{
				for( String metric : metrics )
				{
					//ops.add( new SumOperation( metric ) );
					qc.addOperation( new SumOperation( metric ) );
					//opsCount++;
				}
			}
		}
		
		
		if( query.isRaw( ) )
		{
			//ops.add(  new RawRecordsOperation( )  );
			qc.addOperation( new RawRecordsOperation( ) );
			//opsCount++;
		}
		
		/*
		if( opsCount > 0 )
			qc.addOperation( ops );
		*/

		return qc;
	}
}
