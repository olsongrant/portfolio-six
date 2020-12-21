package com.formulafund.portfolio.data.services.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.Account;
import com.formulafund.portfolio.data.services.AccountService;

@Service
@Profile("map")
public class MapAccountService extends BaseMapService<Account> implements AccountService {

}
