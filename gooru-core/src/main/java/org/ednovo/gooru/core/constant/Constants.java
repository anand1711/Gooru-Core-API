package org.ednovo.gooru.core.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Constants {
	//~ Static fields/initializers =============================================

	/** The name of the configuration hashmap stored in application scope. */
	public static final String CONFIG = "appConfig";
	
	public static final String USER = "USER";
	public static final String CLASS = "CLASS";
	public static final String BOOK = "book";
	public static final String SESSION_TOKEN = "SESSION_TOKEN";
	public static final String SEC_USER = "user";
	public static final String SEC_CLASSPLAN = "classplan";
	public static final String SEC_CONTENT = "content";
	public static final String SEC_STUDYSHELF = "studyshelf";
	public static final String SEC_NOTEBOOK = "notebook";
	public static final String SEC_NOTE = "note";
	public static final String ANONYMOUS = "anonymous";
	public static final String FETCH_TYPE = "fetchType";
	
	public static final String SAKAI_SESSION_ID = "sakaiSessionId";
	
	public static final String REMEBER_ME = "rememberMe";
	public static final String GOORU_HOME = "http://www.goorulearning.org";
	public static final String QA_HOME = "http://qa.goorulearning.org";
	public static final String USER_SESSIONTOKEN ="321579a6-ac12-11e2-871a-123140ff59d0";
	public static final String ADMIN_SESSIONTOKEN ="ff7ed4a0-ac0d-11e2-871a-123140ff59d0";
	public static final String FIRST_LOGIN = "firstLogin";
	public static final String OAUTH_TOKEN = "OAUTH_TOKEN";
	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";
	
	/** The name of the ResourceBundle used in this application */
	public static final String BUNDLE_KEY = "ApplicationResources";
	
	/** The encryption algorithm key to be used for passwords */
	public static final String ENC_ALGORITHM = "algorithm";

	/** A flag to indicate if passwords should be encrypted */
	public static final String ENCRYPT_PASSWORD = "encryptPassword";
	
	/* Classplan Constants */
	/**
	 * The request scope attribute that holds the doc book XSL path
	 */
	public static final String CLASSPLAN_VIEW_XSL_PATH = "/stylesheets/classplan/edit/classplan.xsl";
	
	public static final String SEG_PROPS_VIEW_XSL_PATH = "/stylesheets/classplan/segmentPropsView.xsl";

	public static final String SEGMENTS_VIEW_XSL_PATH = "/stylesheets/classplan/edit/segments.xsl";

	/**
	 * The request scope attribute that holds the doc book XSL path
	 */
	public static final String CLASSPLAN_EDIT_XSL_PATH = "/templates/classplanEdit.xsl";
	
	public static final String ASSET_VIEW_XSL_PATH = "/stylesheets/classplan/viewAsset.xsl";	
	
	public static final String SEG_PROP_EDIT_XSL_PATH = "/stylesheets/classplan/editResource.xsl";

	public static final String CLASSPLAN_REPOSITORY = "repository";
	
	public static final String CLASSPLAN_INDEX = "search/index";
	
	public static final String CLASSPLAN_DIGESTER_RULE = "search/digester/classPlanDigesterRules.xml";
	
	public static final String CLASSPLAN_NO_EDIT_XSL_PATH = "/stylesheets/classplan/view/classplan.xsl";
	
	public static final String CLASSPLAN_EXPORT_XSL_PATH = "/stylesheets/classplan/sync.xsl";
	
	public static final String CLASSPLAN_SYNC_LOCATION = "temp/sync";
	
	/* Classbook Constants */
	
	public static final String MYNOTES_VIEW_XSL_PATH = "/stylesheets/classbook/viewMyNotes.xsl";
	
	public static final String ALLNOTES_VIEW_XSL_PATH = "/stylesheets/classbook/viewAllNotes.xsl";
	
	public static final String VIEW_NOTES_XSL_PATH = "/stylesheets/classbook/viewNotes.xsl";

	public static final String NOTE_VIEW_XSL_PATH = "/stylesheets/classbook/note.xsl";
	
	public static final String VIEW_LESSONPLAN_XSL_PATH = "/stylesheets/classbook/lessonPlan.xsl";
	
	/* Classroom Constants */
	
	public static final String VIEW_CLASSROOM_XSL_PATH = "/stylesheets/classroom/classRoom.xsl";
	
	public static final String VIEW_LIST_CLASSROOM_XSL_PATH = "/stylesheets/classroom/listView.xsl";
	
	public static final String VIEW_SORTER_XSL_PATH = "/stylesheets/classroom/sorterView.xsl";
	
	public static final String VIEW_CLASSPLAN_LIBRARY_XSL_PATH_STEP_1 = "/stylesheets/taxonomyStep1.xsl";
	public static final String VIEW_CLASSPLAN_LIBRARY_XSL_PATH_STEP_2 = "/stylesheets/taxonomyStep2.xsl";
	public static final String VIEW_RESOURCE_LIBRARY_XSL_PATH_STEP_2 = "/stylesheets/taxonomyStep2Resource.xsl";
	
	public static final String VIEW_CLASSPLAN_LIBRARY_XSL_PATH_STEP_3 = "/stylesheets/taxonomyStep3.xsl";
	
	
	public static final String CLASSPLAN_LIBRARY_TREE_XSL_PATH_STEP_3 = "/stylesheets/taxonomyTreeStep3.xsl";
	public static final String CLASSPLAN_LIBRARY_TREE_XSL_PATH_STEP_4 = "/stylesheets/taxonomyTreeStep4.xsl";
	
	//public static final String VIEW_CLASSPLAN_LIBRARY_XSL_PATH_STEP_NEW = "/stylesheets/taxonomy/taxonomyStep22.xsl";
	
	public static final String INDEX_QUESTION_XML = "/stylesheets/resource/createQuestionIndex.xsl";
	
	public static final String VIEW_LEARNGUIDE_PDF = "/stylesheets/classplan/pdf/learnguidePdf.xsl";
	
	public static final String UPLOADED_MEDIA_FOLDER = "uploaded-media";
	
	public static final String UPLOADED_SUMMARY_FOLDER = "summary";
	
	public static final String SEGMENT_FOLDER="segment";
	
	public static final String AUTH_XML = "Auth XML";
	
	public static final String  COPIED_COLLECTION = "copied collection";
	
	public static final String CODE_FOLDER = "code";
	
	public static final String SHELF_TYPE = "Shelf";
	
	public static final String SHELF_SECTION_TYPE = "Folder";
	
	public static final String SHELF_SECTION_DEFAULT_NAME = "General";
	
	public static final String SHELF_DEFAULT_NAME = "'s Favorites";
	
	public static final String REDIS_SUBSCRIBTION = "subscription";
	
	public static final String REDIS_VIEWS = "views";
	
	public static final String REDIS_TOKEN_ENTRY = "tokenEntry";
	
	public static final int HTTP_STAT_INTERNAL_ERROR = 500;

	public static final String EVENT_NAME = "eventName";
	
	public static final String EVENT_PREDICATE = "predicate";
	
	public static final String WSFED_SSO_SESSION_VARIABLE = "wsfed-session-token";

	//search response
	public static final String SEARCH_MULTI_RESOURCE_RESPONSE_WITH_FILTER_FETCHHITSINMULTI = "{\"category\":null,\"searchCount\":0,\"searchInfo\":{\"TextbookTotalHitCount\":0,\"SlideTotalHitCount\":0,\"VideoTotalHitCount\":0,\"ExamTotalHitCount\":0,\"WebsiteTotalHitCount\":0,\"LessonTotalHitCount\":0,\"InteractiveTotalHitCount\":0,\"HandoutTotalHitCount\":0},\"searchResults\":[{\"Textbook\":[],\"Exam\":[],\"Slide\":[],\"Interactive\":[],\"Website\":[],\"Handout\":[],\"Lesson\":[],\"Video\":[]}],\"searchType\":null,\"totalHitCount\":0,\"userInput\":null }";
	
	public static final String SEARCH_MULTI_RESOURCE_RESPONSE = "{\"Textbook\":[],\"Exam\":[],\"Slide\":[],\"Interactive\":[],\"Website\":[],\"Handout\":[],\"Lesson\":[],\"Video\":[]}";
	
	public static final String SEARCH_SINGLE_RESOURCE_RESPONSE="{\"category\":null,\"searchCount\":0,\"searchInfo\":null,\"searchResults\":[],\"searchType\":null,\"totalHitCount\":0,\"userInput\":null}";
	
	public static final String SEARCH_SINGLE_RESPONSE="{\"category\":null,\"queryUId\":null,\"searchCount\":0,\"searchInfo\":null,\"searchResults\":[],\"searchType\":null,\"totalHitCount\":0,\"userInput\":null}";
	
	public static final String SEARCH_QUESTION_RESPONSE="{\"category\":null,\"queryUId\":null,\"searchCount\":0,\"searchInfo\":null,\"searchResults\":[],\"searchType\":null,\"totalHitCount\":0, \"assetURI\" : null}";
	
	public static final List<String> themeUsercustomFieldsKey;
	    static {
	        List<String> customFieldsKeyList = new ArrayList<String>();
	        customFieldsKeyList.add("show_profile_page");
	        customFieldsKeyList.add("organizationAdmin");
	        themeUsercustomFieldsKey = Collections.unmodifiableList(customFieldsKeyList);
	    }
	    
	public static final String JIRA_USERNAME = "jira.username";
	public static final String JIRA_PASSWORD = "jira.password";
}