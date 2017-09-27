package assembler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Composer {

	public static void compose(ArrayList<Slice> slices, String destPath) throws FileNotFoundException {
		String fileName = new String(slices.get(0).getHeader().getSessionID());
		String filePath = destPath + fileName;
		File file = new File(filePath);

		if (file.exists() || file.isDirectory()) {
			throw new InternalError("File already exists");
		}

		FileOutputStream output = new FileOutputStream(file);

		Collections.sort(slices, new Comparator<Slice>() {
			@Override
			public int compare(Slice s1, Slice s2) {
				return s1.getHeader().getIndex() - s2.getHeader().getIndex();
			}
		});

		try {
			for (Slice slice : slices) {
				output.write(slice.getData(), 0, slice.getOriginalSize());
			}
		} catch (IOException e) {
			System.err.println("Error writing file: " + e);
			System.exit(-1);
		} finally {
			try {
				if (output != null)
					output.close();
			} catch (IOException e) {
				System.err.println("IO Exception closing files:");
				e.printStackTrace();
			}
		}
	}
}
