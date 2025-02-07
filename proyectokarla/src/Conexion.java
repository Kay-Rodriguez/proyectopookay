import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Conexion {
    private static final String host = "localhost";
    private static final String port = "27017";
    private static final String nombre_database = "vetgr2";

    private static MongoClient mongoClient;

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            // Conecta al servidor MongoDB
            String connectionString = "mongodb://" + host + ":" + port;
            mongoClient = MongoClients.create(connectionString);
        }
        // Devuelve la conexión a la base de datos
        return mongoClient.getDatabase(nombre_database);
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
