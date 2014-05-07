package com.camelcasing.image.gif.test;

import com.camelcasing.image.gif.OctreeNode;

import org.junit.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OctreeNodeTest{
		
		@Test
		public void octreeChildNull(){
			OctreeNode otn = new OctreeNode(null, 0);
			assertNull(otn.getChildIfExists(5));
		}
		
		@Test
		public void colourCount(){
			OctreeNode otn = new OctreeNode(null, 0);
			OctreeNode child = otn.getChild(5)
					.addColor(12, 12, 12)
					.addColor(12, 12, 12);
			assertEquals(2, child.getColorCount());
			assertEquals(12, child.getBlue());
		}
		
		@Test
		public void mergeNodeUp(){
			OctreeNode otn = new OctreeNode(null, 0);
			otn.addColor(24,  24,  24);
			OctreeNode child = otn.getChild(5)
					.addColor(12, 12, 12)
					.addColor(12, 12, 12);
			child.mergeUp();
			assertEquals(3, otn.getColorCount());
			assertEquals(16, otn.getBlue());
			assertNull(otn.getChildIfExists(5));
		}
		
		@Test
		public void equalsTest(){
			OctreeNode node1 = new OctreeNode(null, 0);
			OctreeNode node2 = new OctreeNode(null, 0);
			assertTrue(node1.equals(node2));
		}
		
		@Test
		public void notEqualTest(){
			OctreeNode node1 = new OctreeNode(null, 1);
			OctreeNode node3 = node1.getChild(0);
			OctreeNode node2 = new OctreeNode(null, 0);
			assertFalse(node2.equals(node3));
		}
		
		@Test
		public void equalsTest2(){
			OctreeNode node1 = new OctreeNode(null, 0);
			OctreeNode node2 = new OctreeNode(null, 0);
			assertTrue(node1.hashCode() == node2.hashCode());
		}
		
		@Test
		public void notEqualTest2(){
			OctreeNode node1 = new OctreeNode(null, 1);
			OctreeNode node2 = new OctreeNode(null, 0);
			assertFalse(node1.hashCode() == node2.hashCode());
		}
}
