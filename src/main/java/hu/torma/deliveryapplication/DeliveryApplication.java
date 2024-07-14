package hu.torma.deliveryapplication;

import hu.torma.deliveryapplication.DTO.ProductDTO;
import hu.torma.deliveryapplication.DTO.SiteDTO;
import hu.torma.deliveryapplication.DTO.UnitDTO;
import hu.torma.deliveryapplication.entity.SecureUser;
import hu.torma.deliveryapplication.repository.SecureUserRepository;
import hu.torma.deliveryapplication.service.ProductService;
import hu.torma.deliveryapplication.service.SiteService;
import hu.torma.deliveryapplication.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.logging.Logger;

@SpringBootApplication
public class DeliveryApplication {
	Logger logger = Logger.getLogger("InitLogger");
	public static void main(String[] args) {
		SpringApplication.run(DeliveryApplication.class, args);
	}

	@Bean
	ServletRegistrationBean jsfServletRegistration (ServletContext servletContext) {

		//spring boot only works if this is set
		servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", Boolean.TRUE.toString());

		//registration
		ServletRegistrationBean srb = new ServletRegistrationBean();
		srb.setServlet(new FacesServlet());
		srb.setUrlMappings(Arrays.asList("*.xhtml"));
		srb.setLoadOnStartup(1);
		return srb;
	}

	@Component("dbInit")
	public class DBInit {
		private ProductService productService;
		private UnitService unitService;
		private SecureUserRepository userRepository;
		private SiteService siteService;

		@Autowired
		public DBInit(UnitService unitService,
					  ProductService productService,
					  SecureUserRepository userRepository,
					  SiteService siteService
					  ) {
			this.unitService = unitService;
			this.productService = productService;
			this.userRepository = userRepository;
			this.siteService = siteService;
			initProductsAndUnits();
			initDefaultUserIfNotExists("username", "password");
			initDefaultSite();
		}

		private void initDefaultSite() {
			if (siteService.getSiteById("-") == null) {
				logger.info("Initializing default '-' site.");
				SiteDTO siteDTO = new SiteDTO();
				siteDTO.setId(0L);
				siteDTO.setSiteName("-");
				siteService.saveSite(siteDTO);
			}
		}

		private void initProductsAndUnits() {
			logger.info("Initializing units and products data for Purchase Tab");
			if (!unitService.existsById("kg")) {
				UnitDTO kgUnit = new UnitDTO();
				kgUnit.setId("kg");
				kgUnit.setUnitName("Kilogramm");
				unitService.saveUnit(kgUnit);
				ProductDTO product = new ProductDTO();
				product.setPrice(672);
				product.setFirstUnit(kgUnit);
				product.setSecondUnit(kgUnit);
				product.setCompPercent(0);
				product.setTariffnum("0");
				product.setId("I.OSZTÁLYÚ");
				productService.saveProduct(product);

				product = new ProductDTO();
				product.setPrice(560);
				product.setFirstUnit(kgUnit);
				product.setSecondUnit(kgUnit);
				product.setCompPercent(0);
				product.setTariffnum("0");
				product.setId("II.OSZTÁLYÚ");
				productService.saveProduct(product);

				product = new ProductDTO();
				product.setPrice(448);
				product.setFirstUnit(kgUnit);
				product.setSecondUnit(kgUnit);
				product.setCompPercent(0);
				product.setTariffnum("0");
				product.setId("III.OSZTÁLYÚ");
				productService.saveProduct(product);

				product = new ProductDTO();
				product.setPrice(224);
				product.setFirstUnit(kgUnit);
				product.setSecondUnit(kgUnit);
				product.setCompPercent(0);
				product.setTariffnum("0");
				product.setId("IV.OSZTÁLYÚ");
				productService.saveProduct(product);

				product = new ProductDTO();
				product.setPrice(56);
				product.setFirstUnit(kgUnit);
				product.setSecondUnit(kgUnit);
				product.setCompPercent(0);
				product.setTariffnum("0");
				product.setId("GYÖKÉR");
				productService.saveProduct(product);

				product = new ProductDTO();
				product.setPrice(300);
				product.setFirstUnit(kgUnit);
				product.setSecondUnit(kgUnit);
				product.setCompPercent(0);
				product.setTariffnum("0");
				product.setId("IPARI");
				productService.saveProduct(product);
			}
		}

		public void initDefaultUserIfNotExists(String username, String password) {
			if (!userRepository.anyUserExists()) {
				logger.info("User was created");
				SecureUser user = new SecureUser(username, "{noop}" + password);
				userRepository.save(user);
			}
		}
	}
}
