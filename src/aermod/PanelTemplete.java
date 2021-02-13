package aermod;

import java.util.Map;

public interface PanelTemplete {
	public void setPanel();
	public void setVisible();
	public void setUnVisible();
	public void setFrames(Map<String, PanelTemplete> frames);
	public void exet(Data data);
}
