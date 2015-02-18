package game;

//TODO add comments

import java.util.Iterator;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Element;

import data.TrapD;
import data.TrapData;

public class Map {
	private int xSize;
	private int ySize;
	private Block[][] map;
	@SuppressWarnings("unused")
	private String background;

	public Map(Element xml) {
		Element mapInfo = xml.getChild("mapInfo");
		if (mapInfo == null)
			throw new NullPointerException(
					"Map Information not found in xml file.");
		try {
			background = mapInfo.getChildText("background");
			int x = Integer.parseInt(mapInfo.getChildText("x"));
			int y = Integer.parseInt(mapInfo.getChildText("y"));

			map = new Block[x][y];
			this.xSize = x;
			this.ySize = y;
			for (int row = 0; row < x; row++) {
				for (int col = 0; col < y; col++) {
					map[row][col] = new Block(row, col);
				}
			}
			List<Element> blocks = xml.getChildren("block");
			Element block, e;
			for (Iterator<Element> it = blocks.iterator(); it.hasNext();) {
				block = it.next();
				x = block.getAttribute("x").getIntValue();
				y = block.getAttribute("y").getIntValue();

				if (block.getChild("untraversable") != null)
					map[x][y].setTraversable(false);

				e = block.getChild("trap");
				if (e != null) {
					TrapD trap = TrapData.getTrapById(e.getAttribute("id")
							.getValue());
					if (trap == null)
						throw new NullPointerException("Trap not found");
					map[x][y].setTrap(trap);
				}

				e = block.getChild("damage");
				if (e != null) {
					map[x][y].setDamage(Integer.parseInt(e.getText()));
					map[x][y].setDamageType(e.getAttributeValue("type"));
				}
				// TODO Add conditions when blocks parameters are added

			}
		} catch (DataConversionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getXSize() {
		return xSize;
	}

	public int getYSize() {
		return ySize;
	}

	public Block[][] getMap() {
		return map;
	}
}
