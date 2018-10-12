package tikape.runko;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import spark.ModelAndView;
import spark.Spark;
import static spark.Spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;
import tikape.runko.database.Database;
import tikape.runko.database.KysymysDao;
import tikape.runko.database.VastausDao;
import tikape.runko.domain.Kysymys;
import tikape.runko.domain.Vastaus;

public class Main {

    public static void main(String[] args) throws Exception {
        
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:kysymyspankki.db");
        //database.init();

        KysymysDao kysymysDao = new KysymysDao(database);
        VastausDao vastausDao = new VastausDao(database);

        Spark.get("/", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kysymykset", kysymysDao.findAll());

            return new ModelAndView(map, "index");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/", (req, res) -> {
            Kysymys kysymys = new Kysymys(-1, req.queryParams("kurssi"), req.queryParams("aihe"), req.queryParams("kysymysteksti"));
            kysymysDao.save(kysymys);
            
            res.redirect("/");
            return "";
        });

        /*get("/opiskelijat", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelijat", opiskelijaDao.findAll());

            return new ModelAndView(map, "opiskelijat");
        }, new ThymeleafTemplateEngine());*/

        Spark.get("/kysymykset/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("kysymys", kysymysDao.findOne(Integer.parseInt(req.params("id"))));
            map.put("vastaukset_vaarin", vastausDao.findAllByKysymysIdAndBooleanFalse(Integer.parseInt(req.params("id"))));
            map.put("vastaukset_oikein", vastausDao.findAllByKysymysIdAndBooleanTrue(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "kysymys");
        }, new ThymeleafTemplateEngine());
        
        Spark.post("/kysymykset/:id", (req, res) -> {
            Integer id = Integer.parseInt(req.params(":id"));
            //Integer kysymysId = Integer.parseInt(req.queryParams("kysymys_id"));
            
            vastausDao.save(new Vastaus(-1, id, req.queryParams("vastausteksti"), false));
            res.redirect("/kysymys");
            return"";
        });
        
        Spark.post("/kysymys/poista/:id", (req, res) -> {
            kysymysDao.delete(Integer.parseInt(req.params(":id")));
            
            res.redirect("/");
            return "";
        });
        
        Spark.post("/vastaus/poista/:id", (req, res) -> {
            vastausDao.delete(Integer.parseInt(req.params(":id")));
            
            res.redirect("/");
            return "";
        });
    }
}
