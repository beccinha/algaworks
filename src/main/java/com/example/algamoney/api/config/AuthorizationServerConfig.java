package com.example.algamoney.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.example.algamoney.api.config.token.CustomTokenEnchancer;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
				.withClient("angular")
				.secret("@ngul@r0")
				.scopes("read", "write")
				.authorizedGrantTypes("password", "refresh_token")
				.refreshTokenValiditySeconds(3600 * 24)
				.accessTokenValiditySeconds(1800)
		.and()
		.withClient("mobile")
		.secret("m0b1l30")
		.scopes("read")
		.authorizedGrantTypes("password", "refresh_token")
		.refreshTokenValiditySeconds(3600 * 24)
		.accessTokenValiditySeconds(1800);
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		
		TokenEnhancerChain tokenEnchancerChain = new TokenEnhancerChain(); //CADEIA DE TOKENS MELHORADOS
		tokenEnchancerChain.setTokenEnhancers(Arrays.asList(tokenEnchancer(), accessTokenConverter()));
				
		endpoints
			.tokenStore(tokenStore())
			.tokenEnhancer(tokenEnchancerChain)
			.reuseRefreshTokens(false) // VAI FAZER COM QUE O REFRESH_TOKEN NÃO EXPIRA E SEMPRE BUSQUE NOVOS
			.authenticationManager(authenticationManager);
	}
	
	

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter acessTokenConverter = new JwtAccessTokenConverter();
		acessTokenConverter.setSigningKey("algaworks");//SENHA PARA O TOKEN NA AUTORIZACAO
		return acessTokenConverter;
	}

	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	private TokenEnhancer tokenEnchancer() {
		return new CustomTokenEnchancer();
	}
	
}