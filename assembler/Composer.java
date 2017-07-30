package assembler;

//import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Composer {

	public static void compose(ArrayList<Slice> slices) {
		// int count = 0, offset = 0;

		Collections.sort(slices, new Comparator<Slice>() {
			@Override
			public int compare(Slice s1, Slice s2) {
				return s1.getHeader().getCount() - s2.getHeader().getCount();
			}
		});

		for (Slice slice : slices) {
			System.out.print(new String(slice.getData()));
		}

		System.out.println();
	}
}
