/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.hl7messagebuilder.api.dao;

import java.lang.reflect.Method;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.module.hl7messagebuilder.api.db.Hl7messagebuilderDAO;
import org.openmrs.module.hl7messagebuilder.api.model.PatientDemographic;
import org.springframework.stereotype.Repository;

@Repository("hl7messagebuilder.Hl7messagebuilderDao")
public class Hl7messagebuilderDao implements Hl7messagebuilderDAO {
	
	private SessionFactory sessionFactory;
	
	/**
	 * @param sessionFactory the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	private org.hibernate.Session getCurrentSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		}
		catch (final NoSuchMethodError ex) {
			try {
				final Method method = this.sessionFactory.getClass().getMethod("getCurrentSession", null);
				return (org.hibernate.Session) method.invoke(this.sessionFactory, null);
			}
			catch (final Exception e) {
				throw new RuntimeException("Failed to get the current hibernate session", e);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PatientDemographic> getPatientDemographicData() {
		final Query query = this.sessionFactory
		        .getCurrentSession()
		        .createSQLQuery(
		            "select 	distinct (pid.identifier) pid,"
		                    + "		pe.gender,"
		                    + "		pe.birthdate,"
		                    + "		pn.given_name,"
		                    + "		pn.middle_name,"
		                    + "		pn.family_name,"
		                    + "		CONCAT(trim(ifnull(pa.address1,'')),' ',trim(ifnull(pa.address2,'')),' ',trim(ifnull(pa.address3,'')),' ',trim(ifnull(pa.address6,'')),' ',trim(ifnull(pa.address5,''))) address,"
		                    + "		pa.state_province," + "		pa.country," + "		pa.county_district," + "		pat.value telefone1,"
		                    + "		pat1.value telefone2," + "		CASE pat2.value" + "   			WHEN 1057 THEN 'S'"
		                    + "   			WHEN 5555 THEN 'M'" + "   			WHEN 1060 THEN 'P'" + "   			WHEN 1059 THEN 'W'"
		                    + "   			WHEN 1056 THEN 'D'" + "   		ELSE 'T'" + "		END marital_status,"
		                    + "		e3.encounter_datetime lastconsultation" + "from" + "person pe"
		                    + "inner join patient p on pe.person_id=p.patient_id" + "left join" + "(   select pid1.*"
		                    + "	from patient_identifier pid1" + "	inner join" + "	("
		                    + "		select patient_id,min(patient_identifier_id) id" + "		from patient_identifier"
		                    + "		where voided=0 and identifier_type=2" + "		group by patient_id" + "	) pid2"
		                    + "	where pid1.patient_id=pid2.patient_id and pid1.patient_identifier_id=pid2.id"
		                    + ") pid on pid.patient_id=p.patient_id" + "left join" + "(	select pn1.*"
		                    + "	from person_name pn1" + "	inner join" + "	(" + "		select person_id,min(person_name_id) id"
		                    + "		from person_name" + "		where voided=0" + "		group by person_id" + "	) pn2"
		                    + "	where pn1.person_id=pn2.person_id and pn1.person_name_id=pn2.id"
		                    + ") pn on pn.person_id=p.patient_id" + "left join" + "(	select pa1.*"
		                    + "	from person_address pa1" + "	inner join" + "	("
		                    + "		select person_id,min(person_address_id) id" + "		from person_address" + "		where voided=0"
		                    + "		group by person_id" + "	) pa2"
		                    + "	where pa1.person_id=pa2.person_id and pa1.person_address_id=pa2.id"
		                    + ") pa on pa.person_id=p.patient_id" + "left join" + "(	select pat1.*"
		                    + "	from person_attribute pat1" + "	inner join" + "	("
		                    + "		select person_id,min(person_attribute_id) id" + "		from person_attribute"
		                    + "		where voided=0 and person_attribute_type_id = 9" + "		group by person_id" + "	) pat2"
		                    + "	where pat1.person_id=pat2.person_id and pat1.person_attribute_id=pat2.id"
		                    + ") pat on pat.person_id=p.patient_id" + "left join" + "(	select pat12.*"
		                    + "	from person_attribute pat12" + "	inner join" + "	("
		                    + "		select person_id,min(person_attribute_id) id" + "		from person_attribute"
		                    + "		where voided=0 and person_attribute_type_id = 14" + "		group by person_id" + "	) pat22"
		                    + "	where pat12.person_id=pat22.person_id and pat12.person_attribute_id=pat22.id"
		                    + ") pat1 on pat1.person_id=p.patient_id" + "left join" + "(	select pat121.*"
		                    + "	from person_attribute pat121" + "	inner join" + "	("
		                    + "		select person_id,min(person_attribute_id) id" + "		from person_attribute"
		                    + "		where voided=0 and person_attribute_type_id = 5" + "		group by person_id" + "	) pat222"
		                    + "	where pat121.person_id=pat222.person_id and pat121.person_attribute_id=pat222.id"
		                    + ") pat2 on pat2.person_id=p.patient_id" + "left join" + "(	select e1.*" + "	from encounter e1"
		                    + "	inner join" + "	(" + "		select patient_id,max(encounter_datetime) last_consultation"
		                    + "		from encounter" + "		where voided=0 and encounter_type = 6" + "		group by patient_id"
		                    + "	) e2" + "	where e1.patient_id=e2.patient_id and e1.encounter_datetime=e2.last_consultation"
		                    + ") e3 on e3.patient_id=p.patient_id" + "where p.voided=0 and pe.voided=0;")
		        .addEntity(PatientDemographic.class);
		return query.list();
	}
}
