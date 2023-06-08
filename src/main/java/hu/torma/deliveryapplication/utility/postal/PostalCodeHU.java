package hu.torma.deliveryapplication.utility.postal;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PostalCodeHU {
    Logger log = Logger.getLogger("Postals");
    File file;
    ClassPathResource res;
    Map<Integer, String> postalInfo = new HashMap<>();


    public String getCityByPostal(Integer postal) {
        return postalInfo.get(postal);
    }


    @PostConstruct
    private void importPostalCodes() {
        try {

            //res = new ClassPathResource("IrszHnk.csv", this.getClass().getClassLoader());
         //   file = new File(res.getURI());
            InputStream fis = this.getClass().getClassLoader().getResourceAsStream("IrszHnk.csv");
            //FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.ISO_8859_1);
            BufferedReader br = new BufferedReader(isr);

            String line = "";
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] postal = line.split(";");
                postalInfo.put(Integer.valueOf(postal[1]), postal[0].replace("\"", ""));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
