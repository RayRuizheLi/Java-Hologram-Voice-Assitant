package model;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class TestingOpeningApplications {

	public static void main(String[] args) throws IOException{
		File file = new File("/Program Files (x86)/Adobe/Acrobat Reader DC/Reader/AcroRd32.exe");
		Desktop.getDesktop().open(file);
	}
}
