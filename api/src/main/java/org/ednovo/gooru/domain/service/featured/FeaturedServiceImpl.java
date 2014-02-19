/////////////////////////////////////////////////////////////
// FeaturedServiceImpl.java
// gooru-api
// Created by Gooru on 2014
// Copyright (c) 2014 Gooru. All rights reserved.
// http://www.goorulearning.org/
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
/////////////////////////////////////////////////////////////
package org.ednovo.gooru.domain.service.featured;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.Comment;

import org.ednovo.gooru.core.api.model.Assessment;
import org.ednovo.gooru.core.api.model.AssessmentQuestion;
import org.ednovo.gooru.core.api.model.Code;
import org.ednovo.gooru.core.api.model.CodeUserAssoc;
import org.ednovo.gooru.core.api.model.Collection;
import org.ednovo.gooru.core.api.model.Content;
import org.ednovo.gooru.core.api.model.FeaturedSet;
import org.ednovo.gooru.core.api.model.FeaturedSetItems;
import org.ednovo.gooru.core.api.model.Learnguide;
import org.ednovo.gooru.core.api.model.Profile;
import org.ednovo.gooru.core.api.model.Resource;
import org.ednovo.gooru.core.api.model.User;
import org.ednovo.gooru.core.application.util.CollectionMetaInfo;
import org.ednovo.gooru.core.constant.ConstantProperties;
import org.ednovo.gooru.core.constant.ParameterProperties;
import org.ednovo.gooru.core.exception.NotFoundException;
import org.ednovo.gooru.domain.service.CollectionService;
import org.ednovo.gooru.domain.service.comment.CommentService;
import org.ednovo.gooru.domain.service.search.SearchResults;
import org.ednovo.gooru.infrastructure.persistence.hibernate.CollectionRepository;
import org.ednovo.gooru.infrastructure.persistence.hibernate.UserRepository;
import org.ednovo.gooru.infrastructure.persistence.hibernate.content.ContentRepository;
import org.ednovo.gooru.infrastructure.persistence.hibernate.customTable.CustomTableRepository;
import org.ednovo.gooru.infrastructure.persistence.hibernate.featured.FeaturedRepository;
import org.ednovo.gooru.infrastructure.persistence.hibernate.question.CommentRepository;
import org.ednovo.gooru.infrastructure.persistence.hibernate.taxonomy.TaxonomyRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeaturedServiceImpl implements FeaturedService, ParameterProperties,ConstantProperties {

	@Autowired
	private FeaturedRepository featuredRepository;

	@Autowired
	private TaxonomyRespository taxonomyRespository;

	@Autowired
	private ContentRepository contentRepository;

	@Autowired
	private CollectionService collectionService;

	@Autowired
	private CollectionRepository collectionRepository;

	@Autowired
	private CustomTableRepository customTableRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	@Override
	public List<FeaturedSet> getFeaturedList(int limit, boolean random, String featuredSetName, String themeCode) throws Exception {
		List<FeaturedSet> featuredSet = this.getFeaturedRepository().getFeaturedList(null, limit, featuredSetName, themeCode, null);
		if (featuredSet != null) {
			this.getFeaturedResource(featuredSet);
		}

		return featuredSet;
	}

	@Override
	public List<FeaturedSet> getFeaturedList(int limit, boolean random, String featuredSetName, String themeCode, String themetype) throws Exception {
		List<FeaturedSet> featuredSet = this.getFeaturedRepository().getFeaturedList(null, limit, featuredSetName, themeCode, themetype);
		if (featuredSet != null) {
			this.getFeaturedResource(featuredSet);
		}

		return featuredSet;
	}

	@Override
	public List<FeaturedSet> getFeaturedTheme(int limit) throws Exception {

		List<FeaturedSet> featuredSet = this.getFeaturedRepository().getFeaturedTheme(limit);

		return featuredSet;
	}

	@Override
	public void getFeaturedResource(List<FeaturedSet> featuredSet) throws Exception {
		for (FeaturedSet featured : featuredSet) {
			List<Resource> resources = new ArrayList<Resource>();
			List<Learnguide> collections = new ArrayList<Learnguide>();
			List<AssessmentQuestion> questions = new ArrayList<AssessmentQuestion>();
			List<Collection> scollections = new ArrayList<Collection>();
			if (featured.getFeaturedSetItems() != null) {
				for (FeaturedSetItems featuredSetItem : featured.getFeaturedSetItems()) {
					if (featuredSetItem.getContent() instanceof AssessmentQuestion) {
						questions.add((AssessmentQuestion) featuredSetItem.getContent());
						if (featuredSetItem.getParentContent() != null && featuredSetItem.getParentContent() instanceof Assessment) {
							((AssessmentQuestion) featuredSetItem.getContent()).setAssessmentGooruId(featuredSetItem.getParentContent().getGooruOid());
						}
					} else if (featuredSetItem.getContent() instanceof Learnguide) {
						collections.add((Learnguide) featuredSetItem.getContent());
					} else if (featuredSetItem.getContent() instanceof Collection) {
						scollections.add((Collection) featuredSetItem.getContent());
					} else if (featuredSetItem.getContent() instanceof Resource) {
						resources.add((Resource) featuredSetItem.getContent());
					}
				}
			}
			featured.setResources(resources);
			featured.setQuestions(questions);
			featured.setCollections(collections);
			featured.setScollections(scollections);
			if (scollections != null) {
				this.setCollectionMetaInfo(scollections);
			}
		}

	}
	
	@Override
	public List<Map<String, Object>> getLibraryCourse(String code, String ChildCode, String libraryName, String rootNode) {
		int collectionCount = 0;
		int unitCount = 0;
		List<Code> courses = this.getTaxonomyRespository().findCodeByParentCodeId(code, null, null, null, true, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
		List<Map<String, Object>> courseMap = new ArrayList<Map<String, Object>>();
		for (Code course : courses) {
			List<Code> units = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(course.getCodeId()), null, null, null, true, LIBRARY, getOrganizationCode(libraryName),  rootNode, null);
			List<Map<String, Object>> unitMap = new ArrayList<Map<String, Object>>();
			unitCount = 0;
			for (Code unit : units) {
				List<Map<String, Object>> topicMap = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> collectionUnitMap = null;
				Integer collectionUnitCount = null;
				List<Object[]> collectionUnitListAll = this.getFeaturedRepository().getLibraryCollection(String.valueOf(unit.getCodeId()), String.valueOf(ChildCode), null, null, true);
				if (collectionUnitListAll != null && collectionUnitListAll.size() > 0) {
					collectionUnitCount = collectionUnitListAll.size();
				}
				if (unitCount == 0) {
					List<Code> topics = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(unit.getCodeId()), null, null, null, true, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
					List<Object[]> collectionUnitList = null;
					collectionUnitList = this.getFeaturedRepository().getLibraryCollection(String.valueOf(unit.getCodeId()), String.valueOf(ChildCode), 10, 0, false);
					if (collectionUnitList != null && collectionUnitList.size() > 0) {
						if (collectionUnitListAll != null && collectionUnitListAll.size() > 0) {
							collectionUnitCount = collectionUnitListAll.size();
						}
						collectionUnitMap = new ArrayList<Map<String, Object>>();
						for (Object[] collectionObject : collectionUnitList) {
							Map<String, Object> collectionUnit = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), new HashMap<String, Object>());
							if (collectionUnit != null) {
								collectionUnitMap.add(collectionUnit);
							}
						}
					}
					if (collectionUnitMap == null || collectionUnitMap.size() == 0) {
						for (Code topic : topics) {
							Integer collectionTopicCount = null;
							List<Object[]> collectionTopicList = this.getFeaturedRepository().getLibraryCollection(String.valueOf(topic.getCodeId()), String.valueOf(ChildCode), 10, 0, false);
							List<Map<String, Object>> collectionTopicMap = null;
							if (collectionTopicList != null && collectionTopicList.size() > 0) {
								List<Object[]> collectionTopicListAll = this.getFeaturedRepository().getLibraryCollection(String.valueOf(topic.getCodeId()), String.valueOf(ChildCode), null, null, true);
								if (collectionTopicListAll != null && collectionTopicListAll.size() > 0) {
									collectionTopicCount = collectionTopicListAll.size();
								}
								collectionTopicMap = new ArrayList<Map<String, Object>>();
								collectionCount = 0;
								for (Object[] collectionObject : collectionTopicList) {
									Map<String, Object> collectionTopic = new HashMap<String, Object>();
									collectionTopic.put(GOORU_OID, collectionObject[0]);
									collectionTopic.put(TITLE, collectionObject[1]);
									if (collectionCount == 0) {
										collectionTopic = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), collectionTopic);
									}
									collectionTopicMap.add(collectionTopic);
									collectionCount++;
								}
							}
							List<Map<String, Object>> lessonMap = new ArrayList<Map<String, Object>>();
							List<Code> allLessons = null;
							if (collectionTopicMap == null || collectionTopicMap.size() == 0) {
								List<Code> lessons = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(topic.getCodeId()), null, 3, 0, false, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
								for (Code lesson : lessons) {
									List<Object[]> collectionList = this.getFeaturedRepository().getLibraryCollection(String.valueOf(lesson.getCodeId()), String.valueOf(ChildCode), null, null, true);
									collectionCount = 0;
									List<Map<String, Object>> collectionMap = new ArrayList<Map<String, Object>>();
									for (Object[] collectionObject : collectionList) {
										Map<String, Object> collection = new HashMap<String, Object>();
										collection.put(GOORU_OID, collectionObject[0]);
										collection.put(TITLE, collectionObject[1]);
										if (collectionCount == 0) {
											collection = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), collection);
										}
										collectionCount++;
										collectionMap.add(collection);
									}
									if (collectionMap != null && collectionMap.size() > 0) {
										lessonMap.add(getCode(lesson, collectionMap, COLLECTION, null, getOrganizationCode(libraryName)));
									}
								}
								allLessons = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(topic.getCodeId()), null, 0, 3, true, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
							}
							topicMap.add(getCode(topic, (collectionTopicMap != null && collectionTopicMap.size() > 0) ? collectionTopicMap : lessonMap, (collectionTopicMap != null && collectionTopicMap.size() > 0) ? COLLECTION : LESSON, collectionTopicCount != null ? collectionTopicCount
									: (allLessons != null ? allLessons.size() : 0), getOrganizationCode(libraryName)));

						}
					}
				}
				unitMap.add(getCode(unit, (collectionUnitCount != null && collectionUnitCount > 0) ? collectionUnitMap : topicMap, (collectionUnitCount != null && collectionUnitCount > 0) ? COLLECTION : TOPIC, collectionUnitCount, getOrganizationCode(libraryName)));
				unitCount++;

			}
			courseMap.add(getCode(course, unitMap, UNIT, null, getOrganizationCode(libraryName)));
		}
		return courseMap;
	}

	@Override
	public Map<Object, Object> getLibrary(String type, String libraryName) {
		List<Object[]> results = this.getFeaturedRepository().getLibrary(null, true, libraryName);
		Map<Object, Object> subjectMap = new HashMap<Object, Object>();
		List<Map<String, Object>> courseMap = new ArrayList<Map<String, Object>>();
		for (Object[] object : results) {
			Map<String, Object> lib = new HashMap<String, Object>();
			lib.put(CODE, object[0] == null ? FEATURED : object[0]);
				if (object[2].equals(type) || (object[0] != null && String.valueOf(object[0]).equalsIgnoreCase(type))) {
					if (object[2].equals(STANDARD)) { 	
						List<Code> curriculums = this.getTaxonomyRespository().findCodeByParentCodeId(null, null, null, null, true, LIBRARY, getOrganizationCode(libraryName), null, "0");
						List<Map<String, Object>> curriculumMap = new ArrayList<Map<String, Object>>();
						int curriculumCount  = 0; 
						for (Code curriculum : curriculums) {
							if (curriculumCount == 0) {
								List<Code> subjects = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(curriculum.getCodeId()), null, null, null, true, LIBRARY, getOrganizationCode(libraryName), String.valueOf(curriculum.getCodeId()), "1");
								for (Code subject : subjects) {
									courseMap = this.getLibraryCourse(String.valueOf(subject.getCodeId()), String.valueOf(object[1]), libraryName, String.valueOf(curriculum.getRootNodeId()));		
									curriculumMap.add(getCode(curriculum, courseMap, "course", null, getOrganizationCode(libraryName)));	
								}
								
							} else { 
								List<Map<String, Object>> courseMap1 = new ArrayList<Map<String, Object>>();
								curriculumMap.add(getCode(curriculum, courseMap1, "course", null, getOrganizationCode(libraryName)));
							}
							curriculumCount++;
						}
						lib.put(DATA_OBJECT, curriculumMap);
					} else { 
						courseMap = this.getLibraryCourse(String.valueOf(lib.get(CODE)), String.valueOf(object[1]), libraryName, "20000");
						lib.put(DATA_OBJECT, courseMap);
					}
				}
				subjectMap.put(object[2], lib);
			}

		return subjectMap;
	}

	@Override
	public List<Map<String, Object>> getLibraryTopic(String topicId, Integer limit, Integer offset, String type, String libraryName, String rootNode) {
		List<Object[]> results = this.getFeaturedRepository().getLibrary(type, false, libraryName);
		String featuredId = null;
		if (results != null && results.size() > 0) {
			Object[] obj = results.get(0);
			featuredId = String.valueOf(obj[1]);
		}
		List<Object[]> collectionTopicList = this.getFeaturedRepository().getLibraryCollection(topicId, featuredId, limit, offset, false);
		List<Map<String, Object>> collectionTopicMap = null;
		if (collectionTopicList != null && collectionTopicList.size() > 0) {
			int collectionCount = 0;
			collectionTopicMap = new ArrayList<Map<String, Object>>();
			for (Object[] collectionObject : collectionTopicList) {
				Map<String, Object> collectionTopic = new HashMap<String, Object>();
				collectionTopic.put(GOORU_OID, collectionObject[0]);
				collectionTopic.put(TITLE, collectionObject[1]);
				if (collectionCount == 0) {
					collectionTopic = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), collectionTopic);
				}
				collectionTopicMap.add(collectionTopic);
				collectionCount++;
			}
			return collectionTopicMap;
		} else {
			List<Map<String, Object>> lessonMap = new ArrayList<Map<String, Object>>();
			List<Code> lessons = this.getTaxonomyRespository().findCodeByParentCodeId(topicId, null, limit, offset, false, libraryName, getOrganizationCode(libraryName), rootNode, null);
			for (Code lesson : lessons) {
				List<Object[]> collectionList = this.getFeaturedRepository().getLibraryCollection(String.valueOf(lesson.getCodeId()), featuredId, null, null, true);
				List<Map<String, Object>> collectionMap = new ArrayList<Map<String, Object>>();
				for (Object[] collectionObject : collectionList) {
					Map<String, Object> collection = new HashMap<String, Object>();
					collection.put(GOORU_OID, collectionObject[0]);
					collection.put(TITLE, collectionObject[1]);
					collectionMap.add(collection);
				}
				if (collectionMap != null && collectionMap.size() > 0) {
					lessonMap.add(getCode(lesson, collectionMap, COLLECTION, null, getOrganizationCode(libraryName)));
				}
			}
			return lessonMap;
		}

	}

	private Map<String, Object> getCode(Code code, List<Map<String, Object>> childern, String type, Integer count, String organizationCode) {
		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put(CODE, code.getCode());
		codeMap.put(CODE_ID, code.getCodeId());
		codeMap.put(CODE_TYPE, code.getCodeType());
		codeMap.put(LABEL, code.getLabel());
		codeMap.put(PARENT_ID, code.getParent() != null ? code.getParent().getCodeId() : null);
		codeMap.put(THUMBNAILS, code.getThumbnails());
		if (code.getDepth() == 2) {
			List<CodeUserAssoc> codeUserAssoc = this.getTaxonomyRespository().getUserCodeAssoc(code.getCodeId(), organizationCode);
			codeMap.put(CREATOR, getUser(codeUserAssoc) != null && getUser(codeUserAssoc).size() > 0 ? getUser(codeUserAssoc).get(0) : null);
			codeMap.put(USER, getUser(codeUserAssoc));
		}
		if (count != null) {
			codeMap.put(COUNT, count);
		}
		codeMap.put(type, childern);
		return codeMap;

	}

	private List<Map<String, String>> getUser(List<CodeUserAssoc> codeUserAssocList) {
		List<Map<String, String>> userMapList = null;
		if (codeUserAssocList != null && codeUserAssocList.size() > 0) {
			userMapList = new ArrayList<Map<String, String>>();
			for (CodeUserAssoc codeUserAssoc : codeUserAssocList) {
				if (codeUserAssoc.getUser() != null) {
					Map<String, String> userMap = new HashMap<String, String>();
					userMap.put(FIRST_NAME, codeUserAssoc.getUser().getFirstName());
					userMap.put(LAST_NAME, codeUserAssoc.getUser().getLastName());
					userMap.put(USER_NAME, codeUserAssoc.getUser().getUsername());
					userMap.put(GOORU_UID, codeUserAssoc.getUser().getPartyUid());
					Profile profile = this.getUserRepository().getProfile(codeUserAssoc.getUser(), false);
					userMap.put(GENDER, (profile != null && profile.getGender() != null) ? profile.getGender().getName() : "");
					userMap.put(IS_OWNER, String.valueOf(codeUserAssoc.getIsOwner()));
					userMapList.add(userMap);
				}
			}
		}
		return userMapList;
	}

	public CommentRepository getCommentRepository() {
		return commentRepository;
	}

	public void setCommentRepository(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	private void setCollectionMetaInfo(List<Collection> collections) {
		for (Collection collection : collections) {
			CollectionMetaInfo collectionMetaInfo = new CollectionMetaInfo();
			collectionMetaInfo.setCourse(this.getCollectionService().getCourse(collection.getTaxonomySet()));
			collectionMetaInfo.setStandards(this.getCollectionService().getStandards(collection.getTaxonomySet()));
			collection.setMetaInfo(collectionMetaInfo);
		}

	}

	@Override
	public FeaturedSet saveOrUpdateFeaturedSet(Integer featuredSetId, String name, Boolean activeFlag, Integer sequence, String themeCode) {
		FeaturedSet featuredSet = null;
		Code code = null;
		if (featuredSetId != null) {
			featuredSet = this.getFeaturedRepository().getFeaturedSetById(featuredSetId);
		} else if (name != null && themeCode != null) {
			featuredSet = this.getFeaturedRepository().getFeaturedSetByThemeNameAndCode(name, themeCode);
		}
		if (featuredSet == null) {
			featuredSet = new FeaturedSet();
		}
		if (name != null) {
			featuredSet.setName(name);
			code = this.getContentRepository().getCodeByName(name);
		}

		if (activeFlag != null) {
			featuredSet.setActiveFlag(activeFlag);
		}
		if (sequence != null) {
			featuredSet.setSequence(sequence);
		}
		if (themeCode != null) {
			featuredSet.setThemeCode(themeCode);
		}
		this.getFeaturedRepository().save(featuredSet);
		return featuredSet;
	}

	@Override
	public FeaturedSetItems saveOrUpdateFeaturedSetItems(FeaturedSet featuredSet, String gooruContentId, Integer featuredSetItemId, String parentGooruContentId, Integer sequence) {

		Content content = this.getContentRepository().findContentByGooruId(gooruContentId);
		Content parentContent = this.getContentRepository().findContentByGooruId(parentGooruContentId);
		FeaturedSetItems featuredSetItems = null;
		if (featuredSet.getFeaturedSetId() != null && sequence != null) {
			featuredSetItems = this.getFeaturedRepository().getFeaturedSetItem(featuredSet.getFeaturedSetId(), sequence);
		}
		if (featuredSetItems == null) {
			featuredSetItems = new FeaturedSetItems();
		}
		if (sequence != null) {
			featuredSetItems.setSequence(sequence);
		}
		if (parentContent != null) {
			featuredSetItems.setParentContent(parentContent);
		}
		if (content != null) {
			featuredSetItems.setContent(content);
		}
		if (featuredSet != null) {
			featuredSetItems.setFeaturedSet(featuredSet);
		}
		this.getFeaturedRepository().save(featuredSetItems);

		return featuredSetItems;
	}

	@Override
	public FeaturedSetItems updateFeaturedContent(String type, Integer featuredSetItemId, FeaturedSetItems newFeaturedSetItems) {
		Content content = this.getContentRepository().findContentByGooruId(newFeaturedSetItems.getContent().getGooruOid());
		if (content == null) {
			throw new NotFoundException("Content not found");
		}
		FeaturedSetItems featuredSetItem = this.getFeaturedRepository().getFeaturedItemByIdAndType(featuredSetItemId, type);
		if (featuredSetItem == null) {
			throw new NotFoundException("featuredSetItem not found");
		}
		if (newFeaturedSetItems.getFeaturedSet() != null && newFeaturedSetItems.getFeaturedSet().getName() != null) {
			featuredSetItem.getFeaturedSet().setName(newFeaturedSetItems.getFeaturedSet().getName());
		}
		featuredSetItem.setContent(content);
		this.getFeaturedRepository().save(featuredSetItem);
		return featuredSetItem;
	}

	@Override
	public List<Map<Object, Object>> getLibraryContributor(String libraryName) {
		List<User> users = this.getTaxonomyRespository().getFeaturedUser(getOrganizationCode(libraryName));
		List<Map<Object, Object>> contributors = new ArrayList<Map<Object, Object>>();
		for (User user : users) {
			if (user != null) {
				Map<Object, Object> contributor = new HashMap<Object, Object>();
				contributor.put(FIRST_NAME, user.getFirstName());
				contributor.put(LAST_NAME, user.getLastName());
				contributor.put(USER_NAME, user.getUsername());
				contributor.put(GOORU_UID, user.getPartyUid());
				Profile profile = this.getUserRepository().getProfile(user, false);
				contributor.put(GENDER, (profile != null && profile.getGender() != null) ? profile.getGender().getName() : "");
				contributor.put(COURSES, this.getTaxonomyRespository().getCodeByDepth(getOrganizationCode(libraryName), Short.valueOf("2"), user.getPartyUid()));
				contributors.add(contributor);
			}
		}
		return contributors;
	}

	@Override
	public List<Map<String, Object>> getLibraryUnit(String unitId, String type, Integer offset, Integer limit, String libraryName, String rootNode) {

		int collectionCount = 0;
		List<Object[]> results = this.getFeaturedRepository().getLibrary(type, false, libraryName);
		String featuredId = null;
		if (results != null && results.size() > 0) {
			Object[] obj = results.get(0);
			featuredId = String.valueOf(obj[1]);
		}
		List<Map<String, Object>> topicMap = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> collectionUnitMap = null;
		List<Object[]> collectionUnitList = this.getFeaturedRepository().getLibraryCollection(unitId, featuredId, limit, offset, false);
		if (collectionUnitList != null && collectionUnitList.size() > 0) {
			collectionUnitMap = new ArrayList<Map<String, Object>>();
			for (Object[] collectionObject : collectionUnitList) {
				Map<String, Object> collectionUnit = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), new HashMap<String, Object>());
				if (collectionUnit != null) {
					collectionUnitMap.add(collectionUnit);
				}
			}

			return collectionUnitMap;
		}
		if (collectionUnitMap == null || collectionUnitMap.size() == 0) {
			List<Code> topics = this.getTaxonomyRespository().findCodeByParentCodeId(unitId, null, null, null, true, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
			for (Code topic : topics) {
				Integer collectionTopicCount = null;
				List<Object[]> collectionTopicList = this.getFeaturedRepository().getLibraryCollection(String.valueOf(topic.getCodeId()), featuredId, 10, 0, false);
				List<Map<String, Object>> collectionTopicMap = null;
				if (collectionTopicList != null && collectionTopicList.size() > 0) {
					List<Object[]> collectionTopicListAll = this.getFeaturedRepository().getLibraryCollection(String.valueOf(topic.getCodeId()), featuredId, null, null, true);
					if (collectionTopicListAll != null && collectionTopicListAll.size() > 0) {
						collectionTopicCount = collectionTopicListAll.size();
					}
					collectionTopicMap = new ArrayList<Map<String, Object>>();
					collectionCount = 0;
					for (Object[] collectionObject : collectionTopicList) {
						Map<String, Object> collectionTopic = new HashMap<String, Object>();
						collectionTopic.put(GOORU_OID, collectionObject[0]);
						collectionTopic.put(TITLE, collectionObject[1]);
						if (collectionCount == 0) {
							collectionTopic = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), collectionTopic);
						}
						collectionTopicMap.add(collectionTopic);
						collectionCount++;
					}
				}
				List<Map<String, Object>> lessonMap = new ArrayList<Map<String, Object>>();
				List<Code> allLessons = null;
				if (collectionTopicMap == null || collectionTopicMap.size() == 0) {
					List<Code> lessons = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(topic.getCodeId()), null, 3, 0, false, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
					for (Code lesson : lessons) {
						List<Object[]> collectionList = this.getFeaturedRepository().getLibraryCollection(String.valueOf(lesson.getCodeId()), featuredId, null, null, true);
						collectionCount = 0;
						List<Map<String, Object>> collectionMap = new ArrayList<Map<String, Object>>();
						for (Object[] collectionObject : collectionList) {
							Map<String, Object> collection = new HashMap<String, Object>();
							collection.put(GOORU_OID, collectionObject[0]);
							collection.put(TITLE, collectionObject[1]);
							if (collectionCount == 0) {
								collection = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), collection);
							}
							collectionCount++;
							collectionMap.add(collection);
						}
						if (collectionMap != null && collectionMap.size() > 0) {
							lessonMap.add(getCode(lesson, collectionMap, COLLECTION, null, getOrganizationCode(libraryName)));
						}
					}
					allLessons = this.getTaxonomyRespository().findCodeByParentCodeId(String.valueOf(topic.getCodeId()), null, 0, 3, true, LIBRARY, getOrganizationCode(libraryName), rootNode, null);
				}
				topicMap.add(getCode(topic, (collectionTopicMap != null && collectionTopicMap.size() > 0) ? collectionTopicMap : lessonMap, (collectionTopicMap != null && collectionTopicMap.size() > 0) ? COLLECTION : LESSON, collectionTopicCount != null ? collectionTopicCount
						: (allLessons != null ? allLessons.size() : 0), getOrganizationCode(libraryName)));

			}
		}
		return topicMap;
	}

	private String getOrganizationCode(String libraryName) {
		if (libraryName != null && libraryName.equalsIgnoreCase("rusd")) {
			return libraryName;
		}

		return "gooru";
	}

	@Override
	public List<Map<String, Object>> getLibraryCollection(Integer id, String type, Integer offset, Integer limit, boolean skipPagination, String libraryName) {
		List<Object[]> results = this.getFeaturedRepository().getLibrary(type, false, libraryName);
		String featuredId = null;
		if (results != null && results.size() > 0) {
			Object[] obj = results.get(0);
			featuredId = String.valueOf(obj[1]);
		}
		List<Map<String, Object>> collectionList = new ArrayList<Map<String, Object>>();
		List<Object[]> result = this.getFeaturedRepository().getLibraryCollection(String.valueOf(id), featuredId,  limit, offset,skipPagination);
		if (result != null && result.size() > 0) {
			for (Object[] object : result) {
				Map<String, Object> collection = new HashMap<String, Object>();
				collection.put(GOORU_OID, object[0]);
				collection.put(TITLE, object[1]);
				collectionList.add(collection);
			}
		}
		return collectionList;
	}
	
	@Override
	public List<Map<String, Object>> getAllLibraryCollections(Integer limit, Integer offset, boolean skipPagination, String themeCode, String themeType) {
		List<Map<String, Object>> collectionList = new ArrayList<Map<String, Object>>();
		List<Object[]> result = this.getFeaturedRepository().getFeaturedCollectionsList(limit, offset, skipPagination, themeCode, themeType);
		if (result != null && result.size() > 0) {
			Map<String, Object> totalHitCount = new HashMap<String, Object>();
			totalHitCount.put("totalHitCount", result.size());
			for (Object[] object : result) {
				Map<String, Object> collection = new HashMap<String, Object>();
				User lastUpdatedUser = this.getUserRepository().findUserByPartyUid(String.valueOf(object[5]));
				Collection featuredCollection = this.getCollectionService().getCollectionByGooruOid(String.valueOf(object[0]), null);
				Long comment = this.getCommentRepository().getCommentCount(String.valueOf(object[0]), null, "notdeleted");
				Long collectionItem = this.getCollectionRepository().getCollectionItemCount(String.valueOf(object[0]), "private,public,anyonewithlink");
				collection.put("searchResults", featuredCollection);
				collection.put("subjectCode", object[13]);
				collection.put("themeCode", object[12]);
				if(lastUpdatedUser != null){
					collection.put(LAST_MODIFIED_BY, lastUpdatedUser.getUsername());
				}
				collection.put(COMMENTS_COUNT, comment);
				collection.put(COLLECTION_ITEM_COUNT, collectionItem);
				//collection.put("totalHitCount", result.size());
				collectionList.add(collection);
			}
			collectionList.add(totalHitCount);
		}
		return collectionList;
	}

	
	@Override
	public List<Map<String, Object>> getPopularLibrary(String courseId,  Integer offset, Integer limit,  String libraryName) {
		List<Object[]> results = this.getFeaturedRepository().getLibrary(courseId, false, libraryName);
		String featuredId = null;
		if (results != null && results.size() > 0) {
			Object[] obj = results.get(0);
			featuredId = String.valueOf(obj[1]);
		} else {
			throw new NotFoundException("popular collection not fund");
		}
		List<Map<String, Object>> collectionUnitMap = null;
		List<Object[]> collectionUnitList = this.getFeaturedRepository().getLibraryCollection(null, featuredId, limit, offset, false);
		collectionUnitMap = new ArrayList<Map<String, Object>>();
		for (Object[] collectionObject : collectionUnitList) {
			Map<String, Object> collectionUnit = this.getCollectionService().getCollection(String.valueOf(collectionObject[0]), new HashMap<String, Object>());
			if (collectionUnit != null) {
				collectionUnitMap.add(collectionUnit);
			}
		}
		return collectionUnitMap;
	}

	public FeaturedRepository getFeaturedRepository() {
		return featuredRepository;
	}

	public TaxonomyRespository getTaxonomyRespository() {
		return taxonomyRespository;
	}

	public ContentRepository getContentRepository() {
		return contentRepository;
	}

	public CollectionService getCollectionService() {
		return collectionService;
	}

	public CollectionRepository getCollectionRepository() {
		return collectionRepository;
	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public UserRepository getUserRepository() {
		return userRepository;
	}

	public CustomTableRepository getCustomTableRepository() {
		return customTableRepository;
	}

}