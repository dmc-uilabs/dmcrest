package org.dmc.services;

public enum DMCError {

	/**
	 * Subclasses or some other grouping mechanism to
	 * group errors for specific services would be nice
	 */

	// Generic
	Generic,

	// SQL
	OtherSQLError,

	// Company
	NotAdminUser,
	NotDMDIIMember,
	CanNotInsertChangeLog,
	CompanySkillSetNotExist,

	//AWS
	AWSError,
	
	// Project
	NotProjectAdmin,
	OnlyProjectAdmin,
	MemberNotAssignedToProject,
	NoExistingRequest,

	// ActiveMQ
	ActiveMQServerURLNotSet,
	CanNotCreateQueue,
	CanNotCloseActiveMQConnection,
	CanNotDeleteQueue,
	CanNotReadMessage,
	
	//DomeAPI
	IncorrectType,
	CanNotGetChildren,
	CanNotGetModel,
	CanNotConnectToDome,

	// Service queries
	ServiceInterfaceNotMatch


}
