
// Keeps script loaded if using script autoloader.
// Global may not be neede...
__config() -> { 'stay_loaded' -> true };

// TODO:
// Add trapdoor check in top position check.
// Creepers can spawn with trapdore in top position.
//
// TODO:
// Add fire check for blockabove.
// Fire will create light, but will go out in the overworld and ender.
// So fire may need to be replaced with none spawnable block if not in the nether.
//
// TODO:
// Drowned spawn in water. Currently ignored.
//
// And probably some other blocks that are spawn proof.
__spawnableblock(b) -> (
	if(solid(b) == false,
		return(false);
	);

	blockabove = pos_offset(b, 'up', 1);
	if(__isairplantorvegetation(blockabove) == false,
		return(false);
	);
	
	blocktwoabove = pos_offset(b, 'up', 2);
	if(__isairplantorvegetation(blocktwoabove) == false,
		return(false);
	);
	
	return(__isspawnableblocktype(b));
);

__isairplantorvegetation(b) -> (
	return(air(b) || material(b) == 'plant' || material(b) == 'vegetation')
);

// NOTE:
// Slime can spawn in any light level. But need three (3 x 3) area.
__ismoblightlevel(b) -> (
	return(block_light(b) < 8)
);

// TODO:
// Add slabs check.
// Currently no slab check.
__isspawnableblocktype(b) -> (
	blockmaterial = material(b);
	if(
		blockmaterial == 'glass',
		(
			return(false);
		),
		blockmaterial == 'carpet',
		(
			return(false);
		),
		blockmaterial == 'redstone_bits',
		(
			return(false);
		)	
	);
	
	if(b == 'bedrock',
		return(false);
	);
	
	return(true);
);

__getblockabove(b) -> pos_offset(b, 'up', 1);

__send_particle_line(from, to) -> (
    particle_line('dust 0.9 0.1 0.1 0.5', 
		from, 
		to, 
		0.5
    );
);

__send_particle(pos) -> (
    particle('dust 0.9 0.1 0.1 0.5',
		pos, 
		30
    );
);