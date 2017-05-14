package brugerautorisation.transport.rmi;

import brugerautorisation.data.Diverse;
import brugerautorisation.data.Bruger;
import java.rmi.Naming;

public class Brugeradminklient {
	public static void main(String[] arg) throws Exception {
//		Brugeradmin ba =(Brugeradmin) Naming.lookup("rmi://localhost/brugeradmin");
		Brugeradmin ba = (Brugeradmin) Naming.lookup("rmi://javabog.dk/brugeradmin");

//    ba.sendGlemtAdgangskodeEmail("jacno", "Dette er en test, husk at skifte kode");
//		ba.ændrAdgangskode("jacno", "kodenj4gvs", "xxx");
//		Bruger b = ba.hentBruger("jacno", "kodenj4gvs");
                Bruger b = ba.hentBruger("s154176", "qwe123");
		System.out.println("Fik bruger = " + b + " Adgangskode:" + b.adgangskode);
		System.out.println("Data: " + Diverse.toString(b));
//		ba.sendEmail("s154176", "qwe123", "Hurra det virker!", "Jeg er så glad");
//              ba.ændrAdgangskode("s154176", "kodevicq1x", "qwe123");

		Object ekstraFelt = ba.getEkstraFelt("s154176", "qwe123", "test");
		System.out.println("Fik ekstraFelt = " + ekstraFelt);

		ba.setEkstraFelt("s154176", "qwe123", "test", "Hej fra Jacob"); // Skriv noget andet her

		String webside = (String) ba.getEkstraFelt("s154176", "qwe123", "webside");
		System.out.println("webside = " + webside);
	}
}
