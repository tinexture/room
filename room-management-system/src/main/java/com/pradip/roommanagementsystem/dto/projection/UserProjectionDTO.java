package com.pradip.roommanagementsystem.dto.projection;

public interface UserProjectionDTO {
	Long getId();
	String getEmail();
	String getFullName();
	String getPassword();
	boolean isEnabled();
	boolean isLocked();
}