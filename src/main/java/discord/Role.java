package discord;

import statics.Permission;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Role class represents a role in server
 * @author  Ghazal
 * @author  Fateme
 * @version 1.0
 * @since   2022-06-28
 */

public class Role implements Serializable {

    //fields
    private String name;
    private ArrayList<Permission> rolePermissions;
    private ArrayList<Integer> permissionIndexes;
    private ArrayList<User> users;


    //constructor
    public Role(String name) {
       rolePermissions=new ArrayList<>();
       this.name=name;
       users=new ArrayList<>();
       permissionIndexes=new ArrayList<>();
    }

    //getter methods


    /**
     * This method is used to access role's name.
     *@param -
     *@return String This returns name
     */
    public String getName() {
        return name;
    }

    /**
     * This method is used to access list of role's permissions.
     *@param -
     *@return ArrayList<Permission> This returns rolePermissions
     */
    public ArrayList<Permission> getRolePermissions() {
        return rolePermissions;
    }

    /**
     * This method is used to access list of users who have this role in server.
     *@param -
     *@return ArrayList<User> This returns users
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * This method is used to access list of role's permission indexes.
     *@param -
     *@return ArrayList<Integer> This returns permissionIndexes
     */

    public ArrayList<Integer> getPermissionIndexes() {
        return permissionIndexes;
    }

    /**
     * This method is used to set an arraylist of users to a role.
     *@param users
     *@return Nothing
     */
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    /**
     * This method is used to set an arraylist of permission indexes to role.
     *@param permissionIndexes
     *@return Nothing
     */
    public void setPermissionIndexes(ArrayList<Integer> permissionIndexes) {
        this.permissionIndexes = permissionIndexes;
    }

    /**
     * This method is used to set an arraylist of permission  to role.
     *@param rolePermissions
     *@return Nothing
     */
    public void setRolePermissions(ArrayList<Permission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }


}
