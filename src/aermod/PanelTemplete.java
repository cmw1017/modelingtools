package aermod;

import java.util.Map;

public interface PanelTemplete {
	public void setPanel(String base_path);
	public void setVisible();
	public void setUnVisible();
	public void setFrames(Map<String, PanelTemplete> frames);
	public void exet(AermodDTO data);
}
