package brugerautorisation.data;
import java.io.*;
import java.util.HashMap;
public class Bruger implements Serializable
{
    // Vigtigt: Sæt versionsnummer så objekt kan læses selvom klassen er ændret!
    private static final long serialVersionUID = 12345; // bare et eller andet nr.
    
    public String brugernavn = "s154176"; // studienummer
    public String email = "thanthony@gmail.com";
    public long sidstAktiv;
    public String campusnetId = "thoant"; // campusnet database-ID
    public String studeretning = "SoftwareTeknologi";
    public String fornavn = "Thomas";
    public String efternavn = "Anthony";
    public String adgangskode = "qwe123";
    public HashMap<String,Object> ekstraFelter = new HashMap<>();
    
    
    public String toString()
    {
        return email;
    }
}