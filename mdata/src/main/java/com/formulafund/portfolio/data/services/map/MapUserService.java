package com.formulafund.portfolio.data.services.map;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.ApplicationUser;
import com.formulafund.portfolio.data.services.UserService;

@Service
@Profile("map")
public class MapUserService extends BaseMapService<ApplicationUser> implements UserService {


}
