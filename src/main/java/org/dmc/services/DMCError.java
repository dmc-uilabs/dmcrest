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
	UserNoCompany,
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
	CannotConnectToDome,

	// Service queries
	ServiceInterfaceNotMatch,
	ServiceIDNotExist,
	UnableToLogServiceHistory, 
	
	//account_server errors
	CannotCreateDOMEServerEntry, 
	UnknownSQLError,
	UnexpectedDOMEError,
	UnexpectedDOMEConnectionError, 
	BadURL,
	UnauthorizedAccessAttempt, 
	CannotPatchDOMEServerEntry,
	CannotDeleteDOMEServerEntry,
	UnknownUser,
	NoContentInQuery,
	CannotChangeServerAccess,
	
	//Individual_Discussions
	InvalidAccountId,
	InvalidCommentId,
	InvalidDiscussionId,
	DiscussionFollowNotFound,
	
	//Date parsing
	ParseError

}
