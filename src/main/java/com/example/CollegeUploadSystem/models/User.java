package com.example.CollegeUploadSystem.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // TODO database migration (increase the first_name column to 50 in the users table).
    private String firstName;

    // TODO database migration (increase the last_name column to 50 in the users table).
    private String lastName;

    // TODO database migration (increase the father_name column to 50 in the users table).
    private String fatherName;

    // TODO database migration (increase the login column to 60 in the users table).
    private String login;
    // TODO database migration (change the password column to 30 in the users table).
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_creator_id")
    private User userCreator;
    private LocalDateTime creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "password_changer_id")
    private User passwordChanger;
    private LocalDateTime passwordChangeTime;

    @ElementCollection(targetClass = UserRoles.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Set<UserRoles> userRoles;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @OneToMany(mappedBy = "user")
    @OrderBy(value = "task_id")
    private List<StudentResult> results;

/*
    public StudentResult getResultByTask(Task task) {
        for (StudentResult result : this.results) {
            if (task.getId() == result.getTask().getId()) {
                return result;
            }
        }

        return null;
    }
*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUserCreator() {
        return userCreator;
    }

    public void setUserCreator(User userCreator) {
        this.userCreator = userCreator;
    }
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public User getPasswordChanger() {
        return passwordChanger;
    }

    public void setPasswordChanger(User passwordChanger) {
        this.passwordChanger = passwordChanger;
    }

    public LocalDateTime getPasswordChangeTime() {
        return passwordChangeTime;
    }

    public void setPasswordChangeTime(LocalDateTime passwordChangeTime) {
        this.passwordChangeTime = passwordChangeTime;
    }

    public Set<UserRoles> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRoles> userRoles) {
        this.userRoles = userRoles;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<StudentResult> getResults() {
        return results;
    }

    public void setResults(List<StudentResult> results) {
        this.results = results;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getUserRoles();
    }

    @Override
    public String getUsername() {
        return this.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.password != null;
    }
}
