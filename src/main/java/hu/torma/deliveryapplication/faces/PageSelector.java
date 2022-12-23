package hu.torma.deliveryapplication.faces;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.logging.Logger;

@Controller("pageSelector")
@SessionScope
public class PageSelector implements Serializable {
    Logger logger = Logger.getLogger("PAGECLOG");
    private String page;


    @PostConstruct
    public void init() {
        this.page = "vendors";
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        logger.info("Selected page: " + page);
        this.page = page;
    }

}
