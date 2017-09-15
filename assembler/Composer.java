package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Composer {

	public static void compose(ArrayList<Slice> slices, File file) throws FileNotFoundException {
		FileOutputStream output = new FileOutputStream(file);

		Collections.sort(slices, new Comparator<Slice>() {
			@Override
			public int compare(Slice s1, Slice s2) {
				return s1.getHeader().getIndex() - s2.getHeader().getIndex();
			}
		});

		try {
			for (Slice slice : slices) {
				output.write(slice.getData(), 0, slice.getHeader().getOriginalSize());
			}
		} catch (IOException e) {
			System.err.println("Error writing file: " + e);
			System.exit(-1);
		} finally {
			try {
				if (output != null)
					output.close();
				System.out.println("Cierro escritura");
			} catch (IOException e) {
				System.err.println("IO Exception closing files:");
				e.printStackTrace();
			}
		}
	}
}
