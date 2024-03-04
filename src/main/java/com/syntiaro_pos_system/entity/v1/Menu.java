package com.syntiaro_pos_system.entity.v1;

import com.syntiaro_pos_system.entity.v1.SubMenu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;
import java.util.List;
import javax.persistence.Table;

@Entity
@Table(name = "Menus")
@Data
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private int menuId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Path")
    private String path;


    @Column(name = "icon")
    private String icon;

    @OneToMany(targetEntity = SubMenu.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id_fk", referencedColumnName = "menu_id")
    private List<SubMenu> subMenu;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<SubMenu> getSubMenu() {
        return subMenu;
    }

    public void setSubMenu(List<SubMenu> subMenu) {
        this.subMenu = subMenu;
    }
}

