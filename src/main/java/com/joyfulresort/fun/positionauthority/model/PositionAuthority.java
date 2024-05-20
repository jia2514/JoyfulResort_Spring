package com.joyfulresort.fun.positionauthority.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.joyfulresort.fun.authorityfunction.model.AuthorityFunction;
import com.joyfulresort.fun.position.model.Position;


@Entity
@Table(name = "position_authority")
@IdClass(PositionAuthority.CompositeDetail.class)
public class PositionAuthority {
    @Id
    @Column(name = "position_id")
    private Integer positionId;  // 對應複合主鍵的一部分

    @Id
    @Column(name = "function_id")
    private Integer functionId;  // 對應複合主鍵的另一部分

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id", referencedColumnName = "position_id", insertable = false, updatable = false)
    private Position position;

//    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "function_id", referencedColumnName = "function_id", insertable = false, updatable = false)
    private AuthorityFunction authorityFunction;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public AuthorityFunction getAuthorityFunction() {
        return authorityFunction;
    }

    public void setAuthorityFunction(AuthorityFunction authorityFunction) {
        this.authorityFunction = authorityFunction;
    }

    // 特別加上對複合主鍵物件的 getter / setter
    public CompositeDetail getCompositeKey() {
        return new CompositeDetail(positionId, functionId);
    }

    public void setCompositeKey(CompositeDetail compositeKey) {
        this.positionId = compositeKey.getPositionId();
        this.functionId = compositeKey.getFunctionId();
    }

   
    @Override
    public String toString() {
        return "CompositeDetail[positionId=" + positionId + ", functionId=" + functionId + "]";
    }

    
    // 需要宣告一個有包含複合主鍵屬性的類別，並一定要實作 java.io.Serializable 介面
    public static class CompositeDetail implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer positionId;
        private Integer functionId;

        // 一定要有無參數建構子
        public CompositeDetail() {
            super();
        }

        public CompositeDetail(Integer positionId, Integer functionId) {
            super();
            this.positionId = positionId;
            this.functionId = functionId;
        }

        public Integer getPositionId() {
            return positionId;
        }

        public void setPositionId(Integer positionId) {
            this.positionId = positionId;
        }

        public Integer getFunctionId() {
            return functionId;
        }

        public void setFunctionId(Integer functionId) {
            this.functionId = functionId;
        }

        // 一定要 override 此類別的 hashCode() 與 equals() 方法！
        @Override
        public int hashCode() {
            return Objects.hash(functionId, positionId);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CompositeDetail other = (CompositeDetail) obj;
            return Objects.equals(functionId, other.functionId) && Objects.equals(positionId, other.positionId);
        }
        
        
        @Override
        public String toString() {
            return "CompositeDetail{" +
                   "positionId=" + positionId +
                   ", functionId=" + functionId +
                   '}';
        }
    }
}