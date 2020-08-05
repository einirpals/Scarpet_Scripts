// Minecraft 1.16.1
// Scarpet 1.6

// Importing functions from shared library.
import('utilities','__spawnableblock', '__isairplantorvegetation', '__ismoblightlevel', '__getblockabove');

// Keeps script loaded if using script autoloader.
__config() -> { 'stay_loaded' -> true };

// public function for testing.
spawnableblocksinspawnsphere_block(cx, cy, cz) -> (
    if(__getspawnableblocks(cx, cy, cz),
        print('Done!');
    )
);

// This is slow as fuck. 40-60s just to loop over every block.
// Well... there are ove 9M blockes to check...
// Using array/list of cords is faster, then using the 'block' function.
__getspawnableblocks(cx, cy, cz) -> (
	// Move center block to true center.
	cx = cx - 0.5;
	cz = cz - 0.5;
	
	radius = 128;
	
	// pi needs to be lowercase...
    // Radius is a littler bit bigger for estimation.
	shperevolume = round((4/3) * pi * (radius+1.75)^3);
	print(str('%s Estimated total blocks to check', shperevolume));

    solidblocks = l();
    totalblocks = 0;
	c_for(tx = -radius, tx < radius, tx += 1,
		c_for(ty = -radius, ty < radius, ty += 1,
			c_for(tz = -radius, tz < radius, tz += 1,
				if(sqrt( tx * tx + ty * ty + tz * tz ) < radius + 2,
                    totalblocks += 1;
					currentblock = block(tx + cx, ty + cy, tz + cz);
                    if(solid(currentblock) && __isairplantorvegetation(__getblockabove(currentblock)), solidblocks += currentblock)
				)
			)
		)
	);

    print(str('%s None air / liquid blocks.', length(solidblocks)));
    print(' --- ');

    spawnablelowlightblocks = l();
	spawnablehighlightblocks = l();
	
	for(solidblocks,
		if(__spawnableblock(_) == true,
			blockabove = __getblockabove(_);	
			if(
				__ismoblightlevel(blockabove) == true,
				(
					spawnablelowlightblocks += blockabove;
				),
				(
					spawnablehighlightblocks += blockabove;
				)
			);
		)
	);

	print(str('%s Total low light spawnable blocks.', length(spawnablelowlightblocks)));
	print(str('%s Total high light spawnable blocks.', length(spawnablehighlightblocks)));
	print(' --- ');
	print(str('%s Total spawnable blocks.', length(spawnablelowlightblocks) + length(spawnablehighlightblocks)));

    return(l(spawnablelowlightblocks, spawnablehighlightblocks));
);