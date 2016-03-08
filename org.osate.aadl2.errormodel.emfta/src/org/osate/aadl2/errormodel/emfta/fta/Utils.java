/**
 * Copyright (c) 2015 Carnegie Mellon University.
 * All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," WITH NO WARRANTIES WHATSOEVER.
 * CARNEGIE MELLON UNIVERSITY EXPRESSLY DISCLAIMS TO THE FULLEST 
 * EXTENT PERMITTEDBY LAW ALL EXPRESS, IMPLIED, AND STATUTORY 
 * WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE WARRANTIES OF 
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, AND 
 * NON-INFRINGEMENT OF PROPRIETARY RIGHTS.

 * This Program is distributed under a BSD license.  
 * Please see license.txt file or permission@sei.cmu.edu for more
 * information. 
 * 
 * DM-0003411
 */

package org.osate.aadl2.errormodel.emfta.fta;

import org.osate.aadl2.NamedElement;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorBehaviorState;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorEvent;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorPropagation;
import org.osate.xtext.aadl2.errormodel.errorModel.ErrorSource;
import org.osate.xtext.aadl2.errormodel.errorModel.FeatureorPPReference;
import org.osate.xtext.aadl2.errormodel.errorModel.TypeSet;
import org.osate.xtext.aadl2.errormodel.util.EMV2Properties;
import org.osate.xtext.aadl2.errormodel.util.EMV2Util;

import edu.cmu.emfta.Event;

public class Utils {
	
	
	/**
	 * Fill an Event with all the properties from the AADL model. Likely, all the related
	 * values in the Hazard property from EMV2.
	 *
	 * @param event					- the event related to the EMV2 artifact
	 * @param component             - the component from the event
	 * @param errorModelArtifact    - the EMV2 artifact (error event, error propagation, etc)
	 * @param typeSet               - the type set (null if none)
	 */
	public static void fillProperties(Event event, ComponentInstance component, NamedElement errorModelArtifact,
			TypeSet typeSet) {
		String propertyDescription;
		propertyDescription = EMV2Properties.getDescription(errorModelArtifact, component);

		if (propertyDescription == null) {
			event.setDescription(getDescription(component, errorModelArtifact, typeSet));
		}
		else
		{
			event.setDescription(propertyDescription + "(component " + component.getName() + ")");
		}

		event.setProbability(EMV2Properties.getProbability(component, errorModelArtifact, typeSet));
	}
	
	
	public static String getDescription (ComponentInstance component, NamedElement errorModelArtifact,
			TypeSet typeSet)
	{
		String description;
		description = "";

		if (errorModelArtifact instanceof ErrorSource)
		{
			ErrorSource errorSource = (ErrorSource) errorModelArtifact;

			description += "Error source";

			if (errorSource.getName() != null)
			{
				 description += " " + errorSource.getName();
			}
			description += " on component " + component.getName();


			if ((errorSource.getOutgoing().getFeatureorPPRef() != null) && (errorSource.getOutgoing().getFeatureorPPRef().getFeatureorPP() != null))
			{
				NamedElement el = errorSource.getOutgoing().getFeatureorPPRef().getFeatureorPP();
				description += " from ";
				description += el.getName();
			}

			description += " with types " + EMV2Util.getPrintName(typeSet);

		}

		if (errorModelArtifact instanceof ErrorEvent)
		{
			ErrorEvent errorEvent = (ErrorEvent) errorModelArtifact;
			description += "Error";
			description += " event " + errorEvent.getName();
			description += " with types " + EMV2Util.getPrintName(typeSet);
			description += " on component " + component.getName();

		}

		if (errorModelArtifact instanceof ErrorBehaviorState)
		{
			ErrorBehaviorState ebs = (ErrorBehaviorState) errorModelArtifact;
			description = "component " + component.getName() + " in state " + ebs.getName();
		}

		return description;
	}
	
	
	public static boolean propagationEndsMatches(ErrorPropagation propagationSource,
			ErrorPropagation propagationDestination) {

		if (EMV2Util.isBinding(propagationSource) && EMV2Util.isBinding(propagationDestination)) {
			return true;
		}

		if ((propagationSource.getFeatureorPPRef() != null) && (propagationDestination.getFeatureorPPRef() != null)
				&& (propagationSource.getFeatureorPPRef().getFeatureorPP() == propagationDestination.getFeatureorPPRef()
				.getFeatureorPP())) {
			return true;
		}

		return false;

	}

	public static String getFeatureFromErrorPropagation(ErrorPropagation errorPropagation) {
// FIXME
//for (FeatureorPPReference fp : EMV2Util.getFeature(errorPropagation)) {
//			return fp.getFeatureorPP().getName();
//		}
		return "unknown feature";
	}
}
