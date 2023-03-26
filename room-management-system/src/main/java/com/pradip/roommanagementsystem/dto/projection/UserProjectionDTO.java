package com.pradip.roommanagementsystem.dto.projection;

public interface UserProjectionDTO {
	Long getId();
	String getUsername();
	String getEmail();
	String getFullName();
	String getPassword();
	boolean isEnabled();
	boolean isLocked();
}