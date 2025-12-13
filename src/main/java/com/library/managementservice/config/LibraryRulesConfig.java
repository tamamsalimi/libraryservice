package com.library.managementservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "library.rules")
public class LibraryRulesConfig {

    private int maxActiveLoansPerMember;
    private int maxLoanDurationDays;

    public int getMaxActiveLoansPerMember() {
        return maxActiveLoansPerMember;
    }

    public void setMaxActiveLoansPerMember(int maxActiveLoansPerMember) {
        this.maxActiveLoansPerMember = maxActiveLoansPerMember;
    }

    public int getMaxLoanDurationDays() {
        return maxLoanDurationDays;
    }

    public void setMaxLoanDurationDays(int maxLoanDurationDays) {
        this.maxLoanDurationDays = maxLoanDurationDays;
    }
}
