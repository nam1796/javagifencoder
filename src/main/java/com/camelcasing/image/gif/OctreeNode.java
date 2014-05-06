package com.camelcasing.image.gif;

public class OctreeNode{

		private int count = 0;
		private OctreeNode[] children = new OctreeNode[8];
		private OctreeNode parent;
		private int red, green, blue;
		private boolean end = false;
	
	public OctreeNode(OctreeNode parent){
		this.parent = parent;
	}

	public OctreeNode addColor(){
		count++;
		return this;
	}
	
	public OctreeNode getChild(int index){
			if(children[index] == null){
				children[index] = new OctreeNode(this);
			}
		return children[index];
	}
	
	public OctreeNode getChildIfExists(int index){
		return children[index];
	}
	
	public void massacreChildren(int r, int g, int b, int c){
		for(int i = 0; i < children.length; i++){
			children[i] = null;
		}
		count += c;
		red = (red += r) / (count != 0 ? count : 1);
		green = (green += g) / (count != 0 ? count : 1);
		blue = (blue += b) / (count != 0 ? count : 1);
		end = true;
	}
	
	public OctreeNode addRGBValue(int r, int g, int b){
		end = true;
		red += r;
		green += g;
		blue += b;
		return this;
	}
	
	public OctreeNode getParent(){
		return parent;
	}
	
	public int getColorCount(){
		return count;
	}
	
	public boolean isEnd(){
		return end;
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}
	
	public void setRed(int r) {
		red = r;
	}

	public void setGreen(int g) {
		green = g;
	}

	public void setBlue(int b) {
		blue = b;
	}
}
