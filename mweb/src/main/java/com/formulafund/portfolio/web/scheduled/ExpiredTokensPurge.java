package com.formulafund.portfolio.web.scheduled;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.formulafund.portfolio.data.services.PasswordResetTokenService;
import com.formulafund.portfolio.data.services.VerificationTokenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ExpiredTokensPurge {
	
	private PasswordResetTokenService passwordTokenService;
	private VerificationTokenService verificationTokenService;
	
	public ExpiredTokensPurge(PasswordResetTokenService aPasswordTokenService,
							  VerificationTokenService aVerificationTokenService) {
		this.passwordTokenService = aPasswordTokenService;
		this.verificationTokenService = aVerificationTokenService;
	}
	
	@Scheduled(cron = "${expired.purge.cron.expression}")
	public void purgeExpired() {
		log.info("Entering ExpiredTokensPurge::purgeExpired");
		this.passwordTokenService.deleteAllExpiredSince(LocalDateTime.now());
		this.verificationTokenService.deleteAllExpiredSince(LocalDateTime.now());
		log.info("Exiting ExpiredTokensPurge::purgeExpired");
	}

}
