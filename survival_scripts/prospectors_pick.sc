// Minecraft 1.16.1
// Scarpet 1.6

// Based on https://github.com/gnembon/scarpet/blob/master/programs/survival/prospectors_pick.sc

// Keeps script loaded if using script autoloader.
// Scripts using 'tick' events, need to have scope global. 
__config() -> { 'stay_loaded' -> true, 'scope' -> 'global' };

__holds(entity, item_regex, enchantment) -> 
(
	if (entity~'gamemode_id' == 3, return(0));
	for(l('mainhand', 'offhand'),
		holds = query(entity, 'holds', _);
		if( holds,
			l(what, count, nbt) = holds;
			if ((what ~ item_regex) && (enchants = get(nbt,'Enchantments[]')),
				// Querying NBT types can return a null for nothing found, value for one item and a list for multiple items.
                // So to simplify code, add the single value to list to loop over.
				if (type(enchants)!='list', enchants = l(enchants));
				for (enchants, 
					if ( get(_,'id') == 'minecraft:' + enchantment,
						level = max(level, get(_,'lvl'))
					)
				)	
			)
		)
	);
	level
);

// 'dust 0.1 0.1 0.1 0.5'
// particle name    -> dust
// RGB              -> 0.1 0.1 0.1
// Size             -> 0.5
global_overworld_ores = l(
	l('coal_ore','dust 0.1 0.1 0.1 0.5'),
	l('iron_ore', 'dust 0.6 0.3 0.1 0.5'),
	l('redstone_ore', 'dust 0.9 0.1 0.1 0.5'),
	
	l('gold_ore','dust 0.9 0.9 0.0 0.5'),
	l('lapis_ore', 'dust 0.1 0.1 1.0 0.5'),
	
	l('diamond_ore','dust 0.3 0.8 1.0 0.5'), 
	l('emerald_ore', 'dust 0.4 1.0 0.4 0.5')
);

global_pick_type = 'golden_pickaxe';
global_prospecting_radius = 16;

__on_tick() -> (
	for (player('!spectating'), player = _;
		if (level = __holds(player, global_pick_type, 'fortune'),
			player_pos = pos(player);
			l(x, y, z) = map(player_pos, floor(_));
			player_in_caves = top('terrain',player_pos) > (y + 3);
			if(player_in_caves,
				loop(round(level * 20 * ((256 - y) / 256)),					
					l(block_x, block_y, block_z) = player_pos + l(rand(global_prospecting_radius), rand(global_prospecting_radius), rand(global_prospecting_radius)) - global_prospecting_radius/2;
					block = block(block_x, block_y, block_z);
					if (block ~ '_ore',
						for(range(level - 1, 1 + 2 * level),
							l(oreblock, ore_particle) = get(global_overworld_ores, _);
							if (block == oreblock,
								particle_line(ore_particle, 
									player_pos + l(0, 1.2, 0), 
									block_x + 0.5, block_y + 0.5, block_z - 0.5, 
									0.8)
							)
						)
					)
				)
			)
		)
	)
);

global_nether_ores = l(
	l('nether_gold_ore','dust 0.9 0.9 0.0 0.5'),
	l('gilded_blackstone','dust 0.9 0.9 0.0 0.5'),

	l('nether_quartz_ore','dust 0.9 0.9 0.9 0.5'),
	l('ancient_debris','dust 0.6 0.3 0.1 0.5'),
);

__on_tick_nether() -> (
	for (player('!spectating'), player = _;
		if (level = __holds(player, global_pick_type, 'fortune'),
			player_pos = pos(player);
			l(x, y, z) = map(player_pos, floor(_));
			loop(round(level * 30 * ((128 - y) / 128)),					
				l(block_x, block_y, block_z) = player_pos + l(rand(global_prospecting_radius), rand(global_prospecting_radius), rand(global_prospecting_radius)) - global_prospecting_radius/2;
				block = block(block_x, block_y, block_z);
				for(range(level - 1, level + 1),
					l(oreblock, ore_particle) = get(global_nether_ores, _);
					if (block == oreblock,
						particle_line(ore_particle, 
							player_pos + l(0, 1.2, 0), 
							block_x + 0.5, block_y + 0.5, block_z - 0.5, 
							0.8)
					)
				)	
			)
		)
	)
);