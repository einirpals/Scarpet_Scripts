// Based on https://github.com/gnembon/scarpet/blob/master/programs/survival/auto_lighter.sc
// Based on https://github.com/gnembon/scarpet/blob/master/programs/survival/prospectors_pick.sc

///right click on a torch while looking at nothing to toggle.

// Importing functions from shared library.
import('utilities','__spawnableblock', '__blockabove', '__send_particle_line', '__send_particle');

// Keeps script loaded if using script autoloader.
__config() -> { 'stay_loaded' -> true };

__on_player_uses_item(player, item, hand) ->
(
	if (hand != 'mainhand', return());
	if (item:0 == 'torch',
		enchant = item:2:'Enchantments[]';
		global_searching_blocks = 0;
		delete(item:2:'Enchantments');
		if (!enchant,
			global_searching_blocks = 1;
			if (enchant==null, item:2 = nbt('{}'));
			put(item:2:'Enchantments','[]');
			put(item:2:'Enchantments', '{lvl:1s,id:"minecraft:protection"}', 0);
			schedule(0, '__search_spawnable_blocks', player);
		);
		inventory_set(player, player~'selected_slot', item:1, item:0, item:2);
	) 
);

global_effect_radius = 6;

__search_spawnable_blocks(player) -> (
    if (global_searching_blocks && player~'holds':0 == 'torch',
		playerpos = pos(player);
		
		radius = global_effect_radius*2;
		numberoflines = rand(4) + 1;

		loop(4000,
			blockpos = playerpos+l(rand(radius), rand(radius), rand(radius)) - radius/2;
			if (__spawnableblock(blockpos),
				
                __send_particle_line(playerpos + l(0, 1.2, 0), __blockabove(blockpos) + l(0.5, 0, 0.5));
                __send_particle(__blockabove(blockpos) + l(0.5, 0, 0.5));

				success += 1;
				if (success > numberoflines, 
					schedule(15, '__search_spawnable_blocks', player);
					return()
				);
			)
		);
		// failed to find a spot, but still have space
		schedule(15, '__search_spawnable_blocks', player)
	)
);