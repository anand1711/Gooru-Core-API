package org.ednovo.gooru.infrastructure.persistence.hibernate.menu;

import java.util.List;

import org.ednovo.gooru.core.api.model.Menu;
import org.ednovo.gooru.core.api.model.MenuItem;
import org.ednovo.gooru.core.api.model.MenuRoleAssoc;
import org.ednovo.gooru.core.api.model.Role;
import org.ednovo.gooru.core.api.model.UserRole;
import org.ednovo.gooru.infrastructure.persistence.hibernate.BaseRepository;

public interface MenuRepository extends BaseRepository{
	
	Menu getMenu(String menuUid);

	List<Menu> listMenu();
	
	List<MenuItem> getMenuItems(List<Integer> roleIds, Integer sequence, String parentMenuUid);
	
	Menu findMenuById(String menuUid);
	
	MenuItem findMenuItemById(String menuItemUid);
	
	Integer getMenuItemCount( String menuUid);
	
    List<MenuItem> getMenuItemsByMenuId(String menuUid);
	    
    Integer getParentMenuCount();
    
    MenuRoleAssoc findMenuRoleAssocEntryByRoleIdAndMenuUid(Integer roleId,String menuUid);
    
    Role findRoleByRoleId(Integer roleId);
	
}
