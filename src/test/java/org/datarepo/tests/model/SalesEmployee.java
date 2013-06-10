package org.datarepo.tests.model;

public class SalesEmployee extends Employee implements Comparable<SalesEmployee> {
    private float commissionRate = 1.0f;

    public SalesEmployee(float commissionRate) {
        this.commissionRate = commissionRate;
    }


    public float getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(float commissionRate) {
        this.commissionRate = commissionRate;
    }

    @Override
    public int compareTo(SalesEmployee o) {
        return 0;
    }
}
