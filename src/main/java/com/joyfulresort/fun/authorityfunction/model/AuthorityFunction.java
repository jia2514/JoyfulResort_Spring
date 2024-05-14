package com.joyfulresort.fun.authorityfunction.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import com.joyfulresort.fun.positionauthority.model.PositionAuthority;

@Entity
@Table(name="authority_function")
public class AuthorityFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="function_id")
    private Integer functionId;
    
    @Pattern(regexp = "^[\u4e00-\u9fa5]{2,10}$", message = "職務名稱: 只能是中文, 且長度必需在2到10之間")
    @Column(name="function_name")
    private String functionName;
    
    
    @OneToMany(mappedBy = "authorityFunction", cascade = CascadeType.ALL)
    @OrderBy("positionId asc")
    private Set<PositionAuthority> positionAuthorities;

    public AuthorityFunction() {
        super();
    }

    public AuthorityFunction(Integer functionId, String functionName, Set<PositionAuthority> positionAuthorities) {
        super();
        this.functionId = functionId;
        this.functionName = functionName;
        this.positionAuthorities = positionAuthorities;
    }

    public Integer getFunctionId() {
        return functionId;
    }

    public void setFunctionId(Integer functionId) {
        this.functionId = functionId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Set<PositionAuthority> getPositionAuthorities() {
        return positionAuthorities;
    }

    public void setPositionAuthorities(Set<PositionAuthority> positionAuthorities) {
        this.positionAuthorities = positionAuthorities;
    }
    
    public String toString() {
    	return "AuthorityFunction:[functionId:"+functionId+"functionName:"+functionName+"]";
    }
    
}
