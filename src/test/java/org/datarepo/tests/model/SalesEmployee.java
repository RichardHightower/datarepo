package org.datarepo.tests.model;

public class SalesEmployee extends Employee {
    private float commissionRate = 1;

    public SalesEmployee(float commissionRate) {
        this.commissionRate = commissionRate;
    }


    public float getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(float commissionRate) {
        this.commissionRate = commissionRate;
    }

}
