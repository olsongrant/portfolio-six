package com.formulafund.portfolio.data.services.map;

import java.util.Optional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.formulafund.portfolio.data.model.CachedPrice;
import com.formulafund.portfolio.data.model.IssuingCompany;
import com.formulafund.portfolio.data.model.Ticker;
import com.formulafund.portfolio.data.services.CachedPriceService;
import com.formulafund.portfolio.data.services.IssuingCompanyService;

@Service
@Profile("map")
public class MapCachedPriceService extends BaseMapService<CachedPrice> implements CachedPriceService {


}
