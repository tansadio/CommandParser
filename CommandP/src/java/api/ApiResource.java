/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import java.math.BigDecimal;
import javax.json.Json;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.regex.*;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author JUX
 */
@Path("api")
public class ApiResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ApiResource
     */
    public ApiResource() {
    }

    /**
     * Retrieves representation of an instance of api.ApiResource
     * @param lang
     * @param str
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson(@QueryParam("lang") String lang, @QueryParam("str") String str) {
        for (String s : str.split(" ")) {
            for (String s2 : new String[]{"écouter", "stopper", "supprimer", "listen", "to", "stop", "remove"}) {
                if (!s.equals(s2) && Utility.Soundex(s).equals(Utility.Soundex(s2))) {
                    str = str.replace(s, s2);
                }
            }
        }
        String[] commands;
        String by;
        if (lang.equals("fr")) {
            commands = new String[]{"écouter", "stopper", "supprimer"};
            by = "de";
        } else if (lang.equals("en")) {
            commands = new String[]{"listen to", "stop", "remove"};
            by = "by";
        } else {
            return Json.createObjectBuilder()
                .add("error", "no lang found")
                .build()
                .toString();
        }
        try {
            Pattern listenPattern = Pattern.compile("^" + commands[0] + "?(( (.+)))");
            //Pattern fromPattern = Pattern.compile(" " + by + "(.+)?$");
            Matcher listenMatcher = listenPattern.matcher(str);
            listenMatcher.find();
            String str2 = listenMatcher.group(0);
            //Matcher fromMatcher = fromPattern.matcher(str2);
            //fromMatcher.find();
            //String str3 = fromMatcher.group(0);
            str2 = str2.replace(commands[0] + " ", "");
            //str3 = str3.substring(4);
            String command;
            String artist;
            String title;
            command = "listen";
            //artist = str3;
            title = str2;
            return Json.createObjectBuilder()
                    .add("command", command)
                    .add("song", Json.createObjectBuilder().add("title",title).build())
                    .build()
                    .toString();
        }catch (Exception e) {
            try {
                Pattern listenPattern = Pattern.compile("^" + commands[1] + "?(( (.+)))");
                //Pattern fromPattern = Pattern.compile(" " + by + "(.+)?$");
                Matcher listenMatcher = listenPattern.matcher(str);
                listenMatcher.find();
                String str2 = listenMatcher.group(0);
                //Matcher fromMatcher = fromPattern.matcher(str2);
                //fromMatcher.find();
                //String str3 = fromMatcher.group(0);
                str2 = str2.replace(commands[1] + " ", "");
                //str3 = str3.substring(4);
                String command;
                String artist;
                String title;
                command = "stop";
                //artist = str3;
                title = str2;
                return Json.createObjectBuilder()
                        .add("command", command)
                        .add("song", Json.createObjectBuilder().add("title",title).build())
                        .build()
                        .toString();
            }catch (Exception e2) {
                        return Json.createObjectBuilder()
                            .add("error", "no match found")
                            .build()
                            .toString();
            }
        }
    }

    /**
     * PUT method for updating or creating an instance of ApiResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
}
