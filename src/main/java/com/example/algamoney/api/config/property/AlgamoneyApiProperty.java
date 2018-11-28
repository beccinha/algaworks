package com.example.algamoney.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("algamoney")
public class AlgamoneyApiProperty {

	//AQUI PODE SEPARAR SOMENTE DO QUE QUER FILTRAR
	private final Seguranca seguranca = new Seguranca();
	
	private String originPermitida = "https://localhost:8000";
	
	public Seguranca getSeguranca() {
		return seguranca;
	}

	public String getOriginPermitida() {
		return originPermitida;
	}

	public void setOriginPermitida(String originPermitida) {
		this.originPermitida = originPermitida;
	}



	public static class Seguranca{ //CRIA ESSA CLASSA PARA PODER SEPARA FUNCOES ESPECIFICA PARA CADA UM DO SETOR DO SITE
		
		private boolean enableHttp;

		public boolean isEnableHttp() {
			return enableHttp;
		}

		public void setEnableHttp(boolean enableHttp) {
			this.enableHttp = enableHttp;
		}
		

	}
	
}
