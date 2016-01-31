package org.assistments.service.dao.impl;

import java.util.ArrayList;
import java.util.List;


import org.assistments.service.dao.DataSourceRegistry;
import org.assistments.service.dao.DataSourceType;
import org.assistments.service.dao.SchemaTable;
import org.assistments.service.dao.SdkDataSource;
import org.assistments.service.dao.UserDao;
import org.assistments.util.Pair;


public enum CoreTableType implements SchemaTable
{
//  PARTNER_TYPES(CoreDataSourceType.CORE, PartnerTypeDao.FieldTypes),
//  PARTNERS(CoreDataSourceType.CORE, PartnerDao.FieldTypes),
//
//  USERS(CoreDataSourceType.CORE, UserDao.FieldTypes, ExternalReferenceType.USER),
//  USER_TYPES(CoreDataSourceType.CORE, UserTypeDao.FieldTypes),
//  USER_ROLE_TYPES(CoreDataSourceType.CORE, UserRoleTypeDao.FieldTypes),
//  USER_ROLES(CoreDataSourceType.CORE, UserRoleDao.FieldTypes),
//  //
//  // GUARDIANS(DatabaseRegistryImpl.CORE, GuardianDao.FieldTypes),
//
//  EXTERNAL_REFERENCE_TYPES(CoreDataSourceType.CORE, ExternalReferenceTypeDao.FieldTypes),
//  EXTERNAL_REFERENCES(CoreDataSourceType.CORE, ExternalReferenceDao.FieldTypes),
//  ACCESS_TOKENS(CoreDataSourceType.CORE, AccessTokenDao.FieldTypes),
//  PARTNER_BRIDGES(CoreDataSourceType.CORE, PartnerBridgeDao.FieldTypes),
//  //
//  PRINCIPAL_TYPES(CoreDataSourceType.CORE, PrincipalTypeDao.FieldTypes),
//  GROUPS(CoreDataSourceType.CORE, GroupDao.FieldTypes, ExternalReferenceType.GROUP),
//  GROUP_MEMBERSHIPS(CoreDataSourceType.CORE, GroupMembershipDao.FieldTypes),
//  //
//  // ACL_PERMISSIONS(DatabaseRegistryImpl.CORE, AclPermissionDao.FieldTypes),
//  // ACL_RESOURCE_TYPES(DatabaseRegistryImpl.CORE, AclResourceTypeDao.FieldTypes),
//  // ACL_VALID_PERMISSIONS(DatabaseRegistryImpl.CORE, AclValidPermissionDao.FieldTypes),
//  // ACL_SIDS(DatabaseRegistryImpl.CORE, AclSidDao.FieldTypes),
//  // ACLS(DatabaseRegistryImpl.CORE, AclDao.FieldTypes),
//  // ACL_ENTRIES(DatabaseRegistryImpl.CORE, AclEntryDao.FieldTypes),
//  //
//  //
//  ASSIGNMENTS(CoreDataSourceType.CORE, AssignmentDao.FieldTypes, ExternalReferenceType.ASSIGNMENT),
//  //
//  // //Actions related tables
//  ASSIGNMENT_LOGS(CoreDataSourceType.CORE, AssignmentLogDao.FieldTypes),
//  ASSIGNMENT_ACTIONS(CoreDataSourceType.CORE, AssignmentActionDao.FieldTypes),
//  ACTION_LEVEL_TYPES(CoreDataSourceType.CORE, ActionLevelTypeDao.FieldTypes),
//  SET_ACTIONS(CoreDataSourceType.CORE, SetActionDao.FieldTypes, ActionLevelType.PROBLEM_SET),
//  PROBLEM_ACTIONS(CoreDataSourceType.CORE, ProblemActionDao.FieldTypes, ActionLevelType.PROBLEM),
//  ACTION_TYPES(CoreDataSourceType.CORE, ActionTypeDao.FieldTypes),
//
//  ACTION_STARTS(CoreDataSourceType.CORE, ActionStartDao.FieldTypes, ActionType.PROBLEM_STARTED_ACTION),
//  TUTOR_STRATEGY_TYPES(CoreDataSourceType.CORE, TutoringTypeDao.FieldTypes),
//  NETWORK_STATE_TYPES(CoreDataSourceType.CORE, NetworkStateTypeDao.FieldTypes),
//
//  ACTION_RESPONSES(CoreDataSourceType.CORE, ActionResponseDao.FieldTypes, ActionType.STUDENT_RESPONSE_ACTION),
//  RESPONSE_VALUE_PARTS(CoreDataSourceType.CORE, ResponseValuePartDao.FieldTypes),
//  RESPONSES(CoreDataSourceType.CORE, ResponseDao.FieldTypes),
//  ANSWER_EVAL_TYPES(CoreDataSourceType.CORE, AnswerEvalTypeDao.FieldTypes),
//
//  ACTION_SUBMITS(CoreDataSourceType.CORE, ActionSubmitDao.FieldTypes, ActionType.STUDENT_SUBMISSION_ACTION),
//  ACTION_HINTS(CoreDataSourceType.CORE, ActionHintDao.FieldTypes, ActionType.HINT_REQUESTED_ACTION),
//  ACTION_URLS(CoreDataSourceType.CORE, ActionUrlDao.FieldTypes, ActionType.URL_LINK_REQUESTED_ACTION),
//  ACTION_SURVEY_RESPONSES(
//    CoreDataSourceType.CORE,
//    ActionSurveyResponseDao.FieldTypes,
//    ActionType.SURVEY_RESPONSE_ACTION),
//  ACTION_ENDS(CoreDataSourceType.CORE, ActionEndDao.FieldTypes, ActionType.PROBLEM_FINISHED_ACTION),

  // Sequences related tables
//  SEQUENCES(CoreDataSourceType.LEGACY, SequenceDao.FieldTypes),
//  SECTIONS(CoreDataSourceType.LEGACY, SectionDao.FieldTypes),
//  ASSISTMENTS(CoreDataSourceType.LEGACY, AssistmentDao.FieldTypes),
//  PROBLEMS(CoreDataSourceType.LEGACY, ProblemDao.FieldTypes),
//  PROBLEM_LOGS(CoreDataSourceType.LEGACY, ProblemLogDao.FieldTypes),
//  QUALITY_LEVELS(CoreDataSourceType.LEGACY, QualityLevelDao.FieldTypes),
//  SEQUENCE_INFOS(CoreDataSourceType.LEGACY, SequenceInfoDao.FieldTypes),
//  VARIABLES(CoreDataSourceType.LEGACY, VariableDao.FieldTypes),
//  ASSISTMENT_TYPES(CoreDataSourceType.LEGACY, AssistmentTypeDao.FieldTypes),
//  ASSISTMENT_OWNERSHIPS(CoreDataSourceType.LEGACY, AssistmentOwnershipDao.FieldTypes),
//  ASSISTMENT_INFOS(CoreDataSourceType.LEGACY, AssistmentInfoDao.FieldTypes),
//  SEQUENCE_OWNERSHIPS(CoreDataSourceType.LEGACY, SequenceOwnershipDao.FieldTypes),
//  HINTS(CoreDataSourceType.LEGACY, HintDao.FieldTypes),
//  SECTION_LINKS(CoreDataSourceType.LEGACY, SectionLinkDao.FieldTypes),
//  SCAFFOLDS(CoreDataSourceType.LEGACY, ScaffoldDao.FieldTypes),
//  TUTOR_STRATEGIES(CoreDataSourceType.LEGACY, TutorStrategyDao.FieldTypes),
//  PROBLEM_TYPES(CoreDataSourceType.LEGACY, ProblemTypeDao.FieldTypes),
//  ANSWERS(CoreDataSourceType.LEGACY, AnswerDao.FieldTypes),
//  USER_GENERATED_HINTS(CoreDataSourceType.LEGACY, UserGeneratedHintDao.FieldTypes);
	USERS(CoreDataSourceType.CORE, UserDao.FieldTypes)
;
  // TODO: Add SHARE_LINKS

  // This is not great and there is probably a better way to pick off whether a table implements is_active.
  // But good enough for now. Use this everywhere
  // TODO: Decide if and how to live w/out this.
  // static final String IS_ACTIVE = "is_active";

  private DataSourceType dsType;
  private SdkDataSource dataSource; 
  private String tableName;
  private List<Pair<Enum<?>, String>> columnDefns;
  private List<String> columnNames = new ArrayList<String>();

  private String pkName;
  private Enum<?>[] types;

  CoreTableType(DataSourceType dbType, List<Pair<Enum<?>, String>> columnDefns)
  {
    this.dsType = dbType;
    this.columnDefns = columnDefns;
    this.types = new Enum<?>[0];
    
    this.tableName = this.name().toLowerCase();
    this.columnNames = SchemaTable.getColumnNames(this.columnDefns);
    this.pkName = SchemaTable.getPkName(this.columnDefns);
  }

  CoreTableType(DataSourceType dbType, List<Pair<Enum<?>, String>> columnDefns, Enum<?>... types)
  {
    this(dbType, columnDefns);
    this.types = types;
  }

  @Override
  public void init(DataSourceRegistry dsr)
  {
    this.dataSource = dsr.getDataSource(this.dsType.getNickname());

    // Keep this at the end so that "this" is fully populated before adding it.
    // In particular addTable() requires that this.tableName have been set
    this.dataSource.addTable(this);
  }

  /**
   * @return the columnDefns
   */
  @Override
  public List<Pair<Enum<?>, String>> getColumnDefns( )
  {
    return columnDefns;
  }

  @Override
  public Enum<?>[] getTypes( )
  {
    return this.types;
  }

  /**
   * @return the data source type
   */
  @Override
  public DataSourceType getDataSourceType( )
  {
    return dsType;
  }

  @Override
  public SdkDataSource getDataSource( )
  {
    return this.dataSource;
  }
  
  @Override
  public String getTableName( )
  {
    return this.tableName;
  }

  @Override
  public String getPrimaryKeyName( )
  {
    return this.pkName;
  }

  /**
   * Returns a list of the table's field (aka column) names. This include the primary key name.
   */
  @Override
  public List<String> getColumnNames( )
  {
    return this.columnNames;
  }
}