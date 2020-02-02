package com.gateway.server.entity;

import lombok.Data;

@Data
public class BloodRequirementData {
	
	private String patientName;
	private String disease;
	private String hospitalName;
	private String location;
	private String city;
	private String state;
	private String contactPersonNumber;
	private String bloodType;
	private String availableHours;
	private String hospitalContactPerson;
	private Double latitude=0d;
	private Double longitude=0d;
}