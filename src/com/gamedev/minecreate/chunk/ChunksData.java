package com.gamedev.minecreate.chunk;

import java.util.HashMap;

import org.joml.*;

public class ChunksData
{
	public ChunksController controller;
	
	public HashMap<Vector3f, Chunk> chunks;
	
	public ChunksData(final ChunksController controller)
	{
		this.controller = controller;
		chunks = new HashMap<Vector3f, Chunk>();
	}
}