package de.pathologie_hh_west.tumordatenbank.data;

import de.pathologie_hh_west.tumordatenbank.logic.Patient;
import de.pathologie_hh_west.tumordatenbank.logic.PatientKey;

public class PatientDAO implements DataAccessObject<PatientKey, Patient> {

	public PatientDAO() {
		super();
	}

	@Override
	public Patient get(PatientKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(Patient obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Patient obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void upsert(Patient obj) {
		// TODO Auto-generated method stub
		
	}
	
}
