package tokenbooking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import tokenbooking.model.ClientCategoryProperties;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class PropertiesController {

    @Autowired
    ClientCategoryProperties clientCategoryProperties;

    @RequestMapping("/properties/{name}")
    public List<String> getAllClientCategory(@PathVariable String name) {
        if (!StringUtils.isEmpty(name) && name.equals("clientcategory")) {
            return clientCategoryProperties.getClientCategory();
        }
        return new ArrayList<>();
    }
}
