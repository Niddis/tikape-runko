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
import tikape.runko.database.OpiskelijaDao;
import tikape.runko.domain.Kysymys;

public class Main {

    public static void main(String[] args) throws Exception {
        
        if (System.getenv("PORT") != null) {
            Spark.port(Integer.valueOf(System.getenv("PORT")));
        }
        
        Database database = new Database("jdbc:sqlite:kysymyspankki.db");
        //database.init();

        //OpiskelijaDao opiskelijaDao = new OpiskelijaDao(database);
        KysymysDao kysymysDao = new KysymysDao(database);

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

        /*get("/opiskelijat/:id", (req, res) -> {
            HashMap map = new HashMap<>();
            map.put("opiskelija", opiskelijaDao.findOne(Integer.parseInt(req.params("id"))));

            return new ModelAndView(map, "opiskelija");
        }, new ThymeleafTemplateEngine());*/
        
        Spark.post("/poista/:id", (req, res) -> {
            // avaa yhteys tietokantaan
            Connection conn
                    = DriverManager.getConnection("jdbc:sqlite:huonekalut.db");

            // tee kysely
            PreparedStatement stmt
            = conn.prepareStatement("DELETE FROM Huonekalu WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(req.params(":id")));

            stmt.executeUpdate();

            // sulje yhteys tietokantaan
            conn.close();

            res.redirect("/");
            return "";
        });
    }
}
